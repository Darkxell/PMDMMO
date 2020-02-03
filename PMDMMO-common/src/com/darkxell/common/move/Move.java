package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffects;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class Move implements Registrable<Move> {
    public enum MoveCategory {
        Physical,
        Special,
        Status;

        public Message getName() {
            return new Message("move.info.category." + this.name());
        }
    }

    /**
     * Move range.<br />
     * <ul>
     * <li>FRONT = 0 ; The Pokemon on the Tile in front of the user.</li>
     * <li>FRONT_ROW = 1 ; The Pokemon on the Tiles in front and diagonals of the user.</li>
     * <li>AROUND = 2 ; All Pokemon within a 1-Tile range of the user.</li>
     * <li>ROOM = 3 ; All Pokemon in a room (or visible, if not in a room.)</li>
     * <li>TWO_TILES = 4 ; The Pokemon on the Tile in front of the user, or if no Pokemon, the one on the second Tile in
     * front.</li>
     * <li>LINE = 5 ; The first Pokemon in the user's direction (up to ten tiles), cuts corners.</li>
     * <li>FLOOR = 6 ; All Pokemon on the Floor.</li>
     * <li>USER = 7 ; Only the user.</li>
     * <li>FRONT_CORNERS = 8 ; The Pokemon on the Tile in front of the user, cuts corners.</li>
     * <li>AMBIENT = 9 ; Does not target any Pokemon.</li>
     * </ul>
     */
    public enum MoveRange {
        /** Does not target any Pokemon. */
        Ambient,
        /** All Pokemon adjacent to the user. */
        Around,
        /** All Pokemon up to two Tiles around the user in all DIRECTIONS. */
        Around2,
        /** All Pokemon on the Floor. */
        Floor,
        /** The Pokemon on the Tile in front of the user. */
        Front,
        /** The Pokemon on the Tile in front of the user, cuts corners. */
        Front_corners,
        /** The Pokemon on the Tiles in front and diagonals of the user. */
        Front_row,
        /** The first Pokemon in the user's direction (up to ten tiles), cuts corners. */
        Line,
        /** A random ally. */
        Random_ally,
        /** All Pokemon in a room (or visible, if not in a room.) */
        Room,
        /** Only the user. */
        Self,
        /** The Pokemon on the Tile in front of the user, or if no Pokemon, the one on the second Tile in front. */
        Two_tiles;

        public Message getName(MoveTarget target) {

            String rangeID = "move.info.range." + this.name();
            if (Localization.containsKey(rangeID + "." + target.name()))
                rangeID += "." + target.name();
            return new Message(rangeID);
        }
    }

    public enum MoveTarget {
        /** Any Pokemon. */
        All,
        /** The user's allies, but not himself. */
        Allies,
        /** Only foes. */
        Foes,
        /** No targets (ambient moves.) */
        None,
        /** Any Pokemon except the user. */
        Others,
        /** The user and its allies. */
        Team,
        /** Only the user. */
        User;

        public boolean isValid(DungeonPokemon user, DungeonPokemon target) {
            switch (this) {
            case Allies:
                return target != user && target.isAlliedWith(user);

            case Foes:
                return !target.isAlliedWith(user);

            case Others:
                return target != user;

            case Team:
                return target == user || target.isAlliedWith(user);

            case User:
                return target == user;

            case None:
                return target == null;

            case All:
            default:
                return true;
            }
        }
    }

    /** This move's accuracy. */
    public final int accuracy;
    /** This move's category. See {@link Move#PHYSICAL}. */
    public final MoveCategory category;
    /** The change of landing critical hits. */
    public final int critical;
    /** True if this move deals damage. */
    public final boolean dealsDamage;
    /** This move's effect. */
    public final int effectID;
    /** True if this move can be boosted by Ginseng. */
    public final boolean ginsengable;
    /** This move's ID. */
    public final int id;
    /** True if this move pierces frozen Pokemon. */
    public final boolean piercesFreeze;
    /** This move's power. */
    public final int power;
    /** This move's default Power Points. */
    public final int pp;
    /** This move's range. */
    public final MoveRange range;
    /** True if this move can be reflected by Magic Coat. */
    public final boolean reflectable;
    /** True if this move can be snatched. */
    public final boolean snatchable;
    /** True if this move is a sound-based move. */
    public final boolean sound;
    /** This move's targets. */
    public final MoveTarget targets;
    /** This move's type. */
    private final PokemonType type;

    Move(int id, PokemonType type, MoveCategory category, int pp, int power, int accuracy, MoveRange range,
            MoveTarget targets, int critical, boolean reflectable, boolean snatchable, boolean sound,
            boolean piercesFreeze, boolean dealsDamage, boolean ginsengable, int effectID) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.pp = pp;
        this.power = power;
        this.accuracy = accuracy;
        this.range = range;
        this.targets = targets;
        this.critical = critical;
        this.reflectable = reflectable;
        this.snatchable = snatchable;
        this.sound = sound;
        this.piercesFreeze = piercesFreeze;
        this.dealsDamage = dealsDamage;
        this.ginsengable = ginsengable;
        this.effectID = effectID;
    }

    @Override
    public int compareTo(Move o) {
        return Integer.compare(this.id, o.id);
    }

    /** @return This Move's description. */
    public Message description() {
        return this.effect().description(this);
    }

    public int displayedPower() {
        return this.power * 5;
    }

    public MoveEffect effect() {
        return MoveEffects.find(this.effectID);
    }

    public int getID() {
        return id;
    }

    public PokemonType getType() {
        return this.type;
    }

    public PokemonType getType(Pokemon pokemon) {
        return this.effect().getMoveType(this, pokemon);
    }

    public boolean hasUseMessage() {
        return Localization.containsKey("move." + this.id);
    }

    /** @return This Move's name. */
    public Message name() {
        return new Message("move." + this.id).addPrefix("<type-" + this.getType().id + "> ").addPrefix("<green>")
                .addSuffix("</color>");
    }

    /**
     * @param  moveEvent - The used move.
     * @return           The Events created by this selection. Creates MoveUseEvents, distributing this Move on targets.
     */
    public final void prepareUse(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        this.effect().prepareUse(moveEvent, events);
    }

    @Override
    public String toString() {
        return this.name().toString().replaceAll("<.*?>", "");
    }

    public Element toXML() {
        Element root = new Element("move");
        root.setAttribute("id", Integer.toString(this.id));
        root.setAttribute("type", Integer.toString(this.type.id));
        root.setAttribute("category", this.category.name());
        root.setAttribute("pp", Integer.toString(this.pp));
        XMLUtils.setAttribute(root, "power", this.power, 0);
        XMLUtils.setAttribute(root, "accuracy", this.accuracy, 100);
        XMLUtils.setAttribute(root, "critical", this.critical, 0);
        XMLUtils.setAttribute(root, "range", this.range.name(), MoveRange.Front.name());
        XMLUtils.setAttribute(root, "targets", this.targets.name(), MoveTarget.Foes.name());
        XMLUtils.setAttribute(root, "effect", this.effectID, 0);
        XMLUtils.setAttribute(root, "damage", this.dealsDamage, false);
        XMLUtils.setAttribute(root, "ginsengable", this.ginsengable, false);
        XMLUtils.setAttribute(root, "freeze", !this.piercesFreeze, false);
        XMLUtils.setAttribute(root, "reflectable", this.reflectable, false);
        XMLUtils.setAttribute(root, "snatchable", this.snatchable, false);
        XMLUtils.setAttribute(root, "sound", this.sound, false);
        return root;
    }

    public Message unaffectedMessage(DungeonPokemon target) {
        return new Message("move.effectiveness.none").addReplacement("<pokemon>", target.getNickname());
    }

    /**
     * Applies this Move's effects to a Pokemon.
     *
     * @param  moveEvent - The Move instance that was selected.
     * @param  events    - The events resulting from this Move. They typically include damage, healing, stat changes...
     * @return           <code>true</code> if the Move missed.
     */
    public boolean useOn(MoveUseEvent moveEvent, ArrayList<Event> events) {
        return this.effect().mainUse(moveEvent, events);
    }
}

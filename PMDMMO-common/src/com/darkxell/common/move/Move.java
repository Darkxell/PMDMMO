package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class Move implements Comparable<Move>
{
	public static enum MoveCategory
	{
		Physical,
		Special,
		Status
	}

	/** Move range.<br />
	 * <ul>
	 * <li>FRONT = 0 ; The Pokémon on the Tile in front of the user.</li>
	 * <li>FRONT_ROW = 1 ; The Pokémon on the Tiles in front and diagonals of the user.</li>
	 * <li>AROUND = 2 ; All Pokémon within a 1-Tile range of the user.</li>
	 * <li>ROOM = 3 ; All Pokémon in a room (or visible, if not in a room.)</li>
	 * <li>TWO_TILES = 4 ; The Pokémon on the Tile in front of the user, or if no Pokémon, the one on the second Tile in front.</li>
	 * <li>LINE = 5 ; The first Pokémon in the user's direction (up to ten tiles), cuts corners.</li>
	 * <li>FLOOR = 6 ; All Pokémon on the Floor.</li>
	 * <li>USER = 7 ; Only the user.</li>
	 * <li>FRONT_CORNERS = 8 ; The Pokémon on the Tile in front of the user, cuts corners.</li>
	 * <li>AMBIENT = 9 ; Does not target any Pokémon.</li>
	 * </ul>
	 */
	public static enum MoveRange
	{
		/** Does not target any Pokémon. */
		Ambient,
		/** All Pokémon adjacent to the user. */
		Around,
		/** All Pokémon up to two Tiles around the user in all directions. */
		Around2,
		/** All Pokémon within a 1-Tile range of the user. */
		Floor,
		/** The Pokémon on the Tile in front of the user. */
		Front,
		/** The Pokémon on the Tile in front of the user, cuts corners. */
		Front_corners,
		/** The Pokémon on the Tiles in front and diagonals of the user. */
		Front_row,
		/** The first Pokémon in the user's direction (up to ten tiles), cuts corners. */
		Line,
		/** All Pokémon in a room (or visible, if not in a room.) */
		Room,
		/** Only the user. */
		Self,
		/** The Pokémon on the Tile in front of the user, or if no Pokémon, the one on the second Tile in front. */
		Two_tiles;
	}

	public static enum MoveTarget
	{
		/** Any Pokémon. */
		All,
		/** The user's allies. */
		Allies,
		/** Only foes. */
		Foes,
		/** No targets (ambient moves.) */
		None,
		/** Any Pokémon except the user. */
		Others,
		/** The user and its allies. */
		Team,
		/** Only the user. */
		User;
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
	private final MoveEffect effect;
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
	public final PokemonType type;

	public Move(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.type = PokemonType.find(Integer.parseInt(xml.getAttributeValue("type")));
		this.category = MoveCategory.valueOf(xml.getAttributeValue("category"));
		this.pp = Integer.parseInt(xml.getAttributeValue("pp"));
		this.power = XMLUtils.getAttribute(xml, "power", 0);
		this.accuracy = XMLUtils.getAttribute(xml, "accuracy", 100);
		this.range = MoveRange.valueOf(XMLUtils.getAttribute(xml, "range", MoveRange.Front.name()));
		this.targets = MoveTarget.valueOf(XMLUtils.getAttribute(xml, "targets", MoveTarget.Foes.name()));
		this.critical = XMLUtils.getAttribute(xml, "critical", -1);
		this.reflectable = XMLUtils.getAttribute(xml, "reflectable", false);
		this.snatchable = XMLUtils.getAttribute(xml, "snatchable", false);
		this.sound = XMLUtils.getAttribute(xml, "sound", false);
		this.piercesFreeze = XMLUtils.getAttribute(xml, "piercesFreeze", false);
		this.dealsDamage = XMLUtils.getAttribute(xml, "damage", false);
		this.ginsengable = XMLUtils.getAttribute(xml, "ginsengable", false);
		this.effect = MoveEffects.find(XMLUtils.getAttribute(xml, "effect", 0));
	}

	public Move(int id, PokemonType type, MoveCategory category, int pp, int power, int accuracy, MoveRange range, MoveTarget targets, int critical,
			boolean reflectable, boolean snatchable, boolean sound, boolean piercesFreeze, boolean tauntable, boolean ginsengable, MoveEffect effect)
	{
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
		this.dealsDamage = tauntable;
		this.ginsengable = ginsengable;
		this.effect = effect;
	}

	@Override
	public int compareTo(Move o)
	{
		return Integer.compare(this.id, o.id);
	}

	/** @return This Move's description. */
	public Message description()
	{
		return new Message("move.info." + this.id);
	}

	/** @return This Move's name. */
	public Message name()
	{
		return new Message("move." + this.id).addPrefix("<type-" + this.type.id + "> ").addPrefix("<green>").addSuffix("</color>");
	}

	/** @param move - The used move.
	 * @param floor - The Floor context.
	 * @return The Events created by this selection. Creates MoveUseEvents, distributing this Move on targets. */
	public final ArrayList<DungeonEvent> prepareUse(MoveUse move, Floor floor)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		this.effect.prepareUse(move, floor, events);
		return events;
	}

	public Element toXML()
	{
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
		XMLUtils.setAttribute(root, "effect", this.effect.id, 0);
		XMLUtils.setAttribute(root, "damage", this.dealsDamage, false);
		XMLUtils.setAttribute(root, "ginsengable", this.ginsengable, false);
		XMLUtils.setAttribute(root, "freeze", this.piercesFreeze, false);
		XMLUtils.setAttribute(root, "reflectable", this.reflectable, false);
		XMLUtils.setAttribute(root, "snatchable", this.snatchable, false);
		XMLUtils.setAttribute(root, "sound", this.sound, false);
		return root;
	}

	protected Message unaffectedMessage(DungeonPokemon target)
	{
		return new Message("move.effectiveness.none").addReplacement("<pokemon>", target.getNickname());
	}

	/** Applies this Move's effects to a Pokémon.
	 * 
	 * @param usedMove - The Move instance that was selected.
	 * @param target - The Pokémon the Move is being used on.
	 * @param floor - The Floor context.
	 * @return The events resulting from this Move. They typically include damage, healing, stat changes... */
	public ArrayList<DungeonEvent> useOn(MoveUse usedMove, DungeonPokemon target, Floor floor)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		this.effect.mainUse(usedMove, target, floor, events);

		return events;
	}
}

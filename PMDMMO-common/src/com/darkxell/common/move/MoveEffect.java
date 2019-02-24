package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class MoveEffect implements AffectsPokemon {

    public static final String DEFAULT_DESCRIPTION_ID = "move.info.default";

    public final int id;

    public MoveEffect(int id) {
        this.id = id;
        if (this.id != -1)
            MoveEffects.effects.put(this.id, this);
    }

    /**
     * This method creates the additional effects (if any) of this Move. Effects should be added to the input MoveEvents
     * list.
     *
     * @param usedMove - The Move Use context.
     * @param target - The Pokemon targeted by the Move.
     * @param flags - The flags of the Event that triggered this use.
     * @param floor - The Floor context.
     * @param calculator - Object that helps with Damage computation.
     * @param missed - <code>true</code> if the Move missed.
     * @param effects - The events list.
     */
    public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
    }

    /** @return <code>true</code> if a Move with this Effect has an effect when it has no targets. */
    protected boolean allowsNoTarget(Move move, DungeonPokemon user) {
        return false;
    }

    /**
     * Creates a custom MoveEffectCalculator object. If the effect doesn't need a custom one, should return null by
     * default.
     *
     * @param usedMove - The Move that was used.
     * @param target - The target of the Move.
     * @param floor - The Floor context.
     * @param flags - The flags of the Move Event.
     * @return The built MoveEffectCalculator, or null.
     */
    public MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags) {
        return null;
    }

    protected void createMoves(MoveUse move, Floor floor, ArrayList<DungeonEvent> events) {
        Move m = move.move.move();

        DungeonPokemon[] pokemon = this.getTargets(m, move.user, floor);
        for (DungeonPokemon element : pokemon)
            this.useOn(move, element, floor, events);
    }

    public Message description(Move move) {
        Message m;
        if (Localization.containsKey("move.info." + this.id))
            m = new Message("move.info." + this.id);
        else
            m = this.descriptionBase(move);
        m.addReplacement("<move>", move.name());
        if (move.dealsDamage)
            m.addPrefix(new Message("move.info.deals_damage").addSuffix(" <br>"));
        return m;
    }

    public Message descriptionBase(Move move) {
        return new Message(DEFAULT_DESCRIPTION_ID);
    }

    /**
     * @param move
     * @param user - The Pokemon using this Move.
     * @param floor - The Floor context.
     * @return The Pokemon affected by this Move.
     */
    public DungeonPokemon[] getTargets(Move move, DungeonPokemon user, Floor floor) {
        ArrayList<DungeonPokemon> targets = new ArrayList<>();
        Tile t = user.tile(), front = t.adjacentTile(user.facing());

        switch (move.range) {
        case Ambient:
            targets.add(null);
            break;

        case Around2:
            for (int x = -2; x < 3; ++x)
                for (int y = -2; y < 3; ++y)
                    if (x == -2 || x == 2 || y == -2 || y == 2) {
                        Tile t2 = floor.tileAt(t.x + x, t.y + y);
                        if (t2.getPokemon() != null)
                            targets.add(t2.getPokemon());
                    }

        case Around:
            for (Direction d : Direction.DIRECTIONS)
                if (t.adjacentTile(d).getPokemon() != null)
                    targets.add(t.adjacentTile(d).getPokemon());
            break;

        case Floor:
            targets.addAll(floor.listPokemon());
            break;

        case Front_row:
            for (Direction d : new Direction[] { user.facing().rotateCounterClockwise(), user.facing(),
                    user.facing().rotateClockwise() })
                if (t.adjacentTile(d).getPokemon() != null)
                    targets.add(t.adjacentTile(d).getPokemon());
            break;

        case Line:
            int distance = 0;
            boolean done;
            Tile current = t;
            do {
                current = current.adjacentTile(user.facing());
                if (current.getPokemon() != null && move.targets.isValid(user, current.getPokemon()))
                    targets.add(current.getPokemon());
                ++distance;
                done = !targets.isEmpty() || distance > 10 || current.isWall();
            } while (!done);
            break;

        case Room:
            Room r = floor.roomAt(user.tile().x, user.tile().y);
            if (r == null) {
                for (Tile tile : floor.aiManager.getAI(user).visibility.currentlyVisibleTiles())
                    if (tile.getPokemon() != null)
                        targets.add(tile.getPokemon());
            } else
                for (Tile t2 : r.listTiles())
                    if (t2.getPokemon() != null)
                        targets.add(t2.getPokemon());
            break;

        case Self:
            targets.add(user);
            break;

        case Two_tiles:
            if (front.getPokemon() != null && move.targets.isValid(user, front.getPokemon()))
                targets.add(front.getPokemon());
            else if (front.type() != TileType.WALL && front.type() != TileType.WALL_END) {
                Tile behind = front.adjacentTile(user.facing());
                if (behind.getPokemon() != null)
                    targets.add(behind.getPokemon());
            }
            break;

        case Random_ally:
            ArrayList<DungeonPokemon> candidates = new ArrayList<>(floor.listPokemon());
            candidates.removeIf(p -> p == user || !user.isAlliedWith(p) || p.type == DungeonPokemonType.RESCUEABLE);
            if (!candidates.isEmpty())
                targets.add(RandomUtil.random(candidates, floor.random));
            break;

        case Front:
        case Front_corners:
        default:
            DungeonPokemon f = user.tile().adjacentTile(user.facing()).getPokemon();
            if (f != null) {
                boolean valid = true;
                if (user.facing().isDiagonal() && move.range != MoveRange.Front_corners) {
                    Tile t1 = user.tile().adjacentTile(user.facing().rotateClockwise());
                    if (t1.isWall())
                        valid = false;
                    t1 = user.tile().adjacentTile(user.facing().rotateCounterClockwise());
                    if (t1.isWall())
                        valid = false;
                }
                if (valid)
                    targets.add(f);
            }
        }

        if (!user.hasStatusCondition(StatusConditions.Confused))
            targets.removeIf(p -> !move.targets.isValid(user, p));
        if (move.range == MoveRange.Room || move.range == MoveRange.Floor)
            targets.sort(floor.dungeon::compare);
        if (targets.isEmpty() && this.allowsNoTarget(move, user))
            targets.add(null);

        return targets.toArray(new DungeonPokemon[0]);
    }

    /**
     * This method creates the main effects of this Move. This method shouldn't be overridden unless you want to change
     * the basics of how Moves work. Effects should be added to the input MoveEvents list.
     *
     * @param usedMove - The Move Use context.
     * @param target - The Pokemon targeted by the Move.
     * @param flags - The flags of the Event that triggered this use.
     * @param floor - The Floor context.
     * @param calculator - Object that helps with Damage computation.
     * @param missed - <code>true</code> if the Move missed.
     * @param effects - Resulting Events list.
     */
    protected void mainEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        if (target != null)
            if (missed)
                effects.createEffect(new MessageEvent(floor,
                        eventSource, new Message(target == null ? "move.miss.no_target" : "move.miss").addReplacement("<pokemon>",
                                target == null ? new Message("no one", false) : target.getNickname())),
                        usedMove, target, floor, missed, true, null);
            else if (usedMove.move.move().dealsDamage)
                effects.createEffect(new DamageDealtEvent(floor, target, usedMove, DamageType.MOVE,
                        calculator.compute(effects.events)), usedMove, target, floor, missed, false, null);

        this.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);
    }

    /**
     * Main method called when a Pokemon uses a Move on a target.
     *
     * @return <code>true</code> if the Move missed.
     */
    public boolean mainUse(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            ArrayList<DungeonEvent> events) {
        Move move = usedMove.move.move();
        MoveEffectCalculator calculator = this.buildCalculator(usedMove, target, floor, flags);
        if (calculator == null)
            calculator = new MoveEffectCalculator(usedMove, target, floor, flags);
        boolean missed = calculator.misses(events);
        double effectiveness = calculator.effectiveness();
        if (effectiveness == PokemonType.NO_EFFECT && usedMove.move.move().category != MoveCategory.Status)
            events.add(new MessageEvent(floor, eventSource, move.unaffectedMessage(target)));
        else {
            if (!missed && this != MoveEffects.Basic_attack && target != null)
                target.receiveMove(usedMove.move.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
            if (!missed && move.dealsDamage)
                if (effectiveness >= PokemonType.SUPER_EFFECTIVE)
                    events.add(new MessageEvent(floor,
                            eventSource, new Message("move.effectiveness.super").addReplacement("<pokemon>", target.getNickname())));
                else if (effectiveness <= PokemonType.NOT_VERY_EFFECTIVE)
                    events.add(new MessageEvent(floor, eventSource, new Message("move.effectiveness.not_very")
                            .addReplacement("<pokemon>", target.getNickname())));

            MoveEvents effects = new MoveEvents();
            this.mainEffects(usedMove, target, flags, floor, calculator, missed, effects);
            events.addAll(effects.events);
        }
        return missed;
    }

    public void prepareUse(MoveUse move, Floor floor, ArrayList<DungeonEvent> events) {
        this.createMoves(move, floor, events);
        if (events.size() == 0 && this != MoveEffects.Basic_attack)
            events.add(new MessageEvent(floor, eventSource, new Message("move.no_target")));
    }

    /**
     * Creates the MoveUseEvent and adds it to the events list.
     *
     * @param move - The Move use context.
     * @param target - The target Pokemon.
     * @param floor - The Floor context.
     * @param events - The event list to add the MoveUseEvent to.
     */
    protected void useOn(MoveUse move, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events) {
        events.add(new MoveUseEvent(floor, eventSource, move, target));
    }

}

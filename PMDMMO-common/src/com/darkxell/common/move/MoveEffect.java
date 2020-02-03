package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.behavior.MoveTargetSelector;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;
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
     * @param moveEvent  - The Move Use context.
     * @param calculator - Object that helps with Damage computation.
     * @param missed     - <code>true</code> if the Move missed.
     * @param effects    - The events list.
     */
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
    }

    /**
     * Can be overridden to create additional events whenever the move is used, whether or not it hits or has a target.
     * 
     * @param moveEvent - The Move Selection Event source
     * @param move      - The Move that was used
     * @param events    - The resulting events list.
     */
    public void additionalEffectsOnUse(MoveSelectionEvent moveEvent, Move move, ArrayList<Event> events) {
    }

    /** @return <code>true</code> if a Move with this Effect has an effect when it has no targets. */
    public boolean allowsNoTarget(Move move, DungeonPokemon user) {
        return false;
    }

    /**
     * Creates a custom MoveEffectCalculator object. If the effect doesn't need a custom one, should return null by
     * default.
     *
     * @param  moveEvent - The Move that was used.
     * @return           The built MoveEffectCalculator, or null.
     */
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return null;
    }

    protected void createMoves(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        Move m = moveEvent.usedMove().move.move();

        DungeonPokemon[] pokemon = new MoveTargetSelector(moveEvent.floor, m, moveEvent.usedMove().user).select();
        for (DungeonPokemon element : pokemon)
            this.useOn(moveEvent, element, events);

        this.additionalEffectsOnUse(moveEvent, m, events);
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

    public PokemonType getMoveType(Move move, Pokemon user) {
        return move.getType();
    }

    /**
     * This method creates the main effects of this Move. This method shouldn't be overridden unless you want to change
     * the basics of how Moves work. Effects should be added to the input MoveEvents list.
     *
     * @param moveEvent  - The Move Use context.
     * @param calculator - Object that helps with Damage computation.
     * @param missed     - <code>true</code> if the Move missed.
     * @param effects    - Resulting Events list.
     */
    protected void mainEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        if (moveEvent.target != null)
            if (missed)
                effects.createEffect(new MessageEvent(moveEvent.floor, moveEvent,
                        new Message(moveEvent.target == null ? "move.miss.no_target" : "move.miss")
                                .addReplacement("<pokemon>", moveEvent.target == null ? new Message("no one", false)
                                        : moveEvent.target.getNickname())),
                        true);
            else if (moveEvent.usedMove.move.move().dealsDamage)
                effects.createEffect(new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.target,
                        moveEvent.usedMove, DamageType.MOVE, calculator.compute(effects.events)), false);

        this.additionalEffects(moveEvent, calculator, missed, effects);
    }

    /**
     * Main method called when a Pokemon uses a Move on a target.
     *
     * @return <code>true</code> if the Move missed.
     */
    public boolean mainUse(MoveUseEvent moveEvent, ArrayList<Event> events) {
        Move move = moveEvent.usedMove.move.move();
        MoveEffectCalculator calculator = this.buildCalculator(moveEvent);
        if (calculator == null)
            calculator = new MoveEffectCalculator(moveEvent);
        boolean missed = calculator.misses(events);
        double effectiveness = calculator.effectiveness();
        if (effectiveness == PokemonType.NO_EFFECT && moveEvent.usedMove.move.move().category != MoveCategory.Status)
            events.add(new MessageEvent(moveEvent.floor, moveEvent, move.unaffectedMessage(moveEvent.target)));
        else {
            if (!missed && this != MoveEffects.Basic_attack && moveEvent.target != null)
                moveEvent.target.receiveMove(
                        moveEvent.usedMove.move.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
            if (!missed && move.dealsDamage)
                if (effectiveness >= PokemonType.SUPER_EFFECTIVE)
                    events.add(new MessageEvent(moveEvent.floor, moveEvent, new Message("move.effectiveness.super")
                            .addReplacement("<pokemon>", moveEvent.target.getNickname())));
                else if (effectiveness <= PokemonType.NOT_VERY_EFFECTIVE)
                    events.add(new MessageEvent(moveEvent.floor, moveEvent, new Message("move.effectiveness.not_very")
                            .addReplacement("<pokemon>", moveEvent.target.getNickname())));

            MoveEvents effects = new MoveEvents();
            this.mainEffects(moveEvent, calculator, missed, effects);
            events.addAll(effects.events);
        }
        return missed;
    }

    public void prepareUse(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        this.createMoves(moveEvent, events);
        if (events.size() == 0 && this != MoveEffects.Basic_attack)
            events.add(new MessageEvent(moveEvent.floor, moveEvent, new Message("move.no_target")));
    }

    /**
     * Creates the MoveUseEvent and adds it to the events list.
     *
     * @param moveEvent - The Move use context.
     * @param target    - The target Pokemon.
     * @param events    - The event list to add the MoveUseEvent to.
     */
    protected void useOn(MoveSelectionEvent moveEvent, DungeonPokemon target, ArrayList<Event> events) {
        events.add(new MoveUseEvent(moveEvent.floor, moveEvent, moveEvent.usedMove(), target));
    }
}

package com.darkxell.common.move.effect;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.language.Message;

public abstract class MoveEffect implements AffectsPokemon, Comparable<MoveEffect> {

    public final int priority;

    public MoveEffect() {
        this(1);
    }

    public MoveEffect(int priority) {
        this.priority = priority;
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

    public PokemonType alterMoveType(Move move, Pokemon pokemon) {
        return null;
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

    @Override
    public int compareTo(MoveEffect o) {
        return Integer.compare(this.priority, o.priority);
    }

    public Message description() {
        return new Message("", false);
    }

    /**
     * This method creates the effects (if any) of this Move. Effects should be added to the input events list.
     *
     * @param moveEvent         - The Move Use context.
     * @param calculator        - Object that helps with Damage computation.
     * @param missed            - <code>true</code> if the Move missed.
     * @param effects           - The events list.
     * @param createAdditionals - True if this method should create additional effects, false if it should create main
     *                          effects.
     */
    public abstract void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals);

    /** @return <code>true</code> if a Move with this Effect has an effect when it has no targets. */
    public boolean hasEffectWithoutTarget(Move move, DungeonPokemon user) {
        return false;
    }
}

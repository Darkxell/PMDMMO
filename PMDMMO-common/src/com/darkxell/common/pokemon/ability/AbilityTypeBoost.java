package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.PokemonType;

/** An ability that boosts moves of a certain type when the user has 1/4 of its HP or less. */
public class AbilityTypeBoost extends Ability {

    /** The type of moves to boost. */
    public final PokemonType type;

    public AbilityTypeBoost(int id, PokemonType type) {
        super(id);
        this.type = type;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveContext context, ArrayList<Event> events) {
        if (isUser && context.user.getHpPercentage() < .25
                && context.move.getType(context.user.usedPokemon) == this.type) {
            events.add(new TriggeredAbilityEvent(context.floor, context.event, context.user));
            return 2;
        }
        return 1;
    }

}

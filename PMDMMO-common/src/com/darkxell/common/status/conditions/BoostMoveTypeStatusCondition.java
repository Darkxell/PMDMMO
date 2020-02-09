package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;

public class BoostMoveTypeStatusCondition extends StatusCondition {

    public final PokemonType type;

    public BoostMoveTypeStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, PokemonType type) {
        super(id, isAilment, durationMin, durationMax);
        this.type = type;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveContext context, ArrayList<Event> events) {
        if (isUser && context.move.getType(context.user.usedPokemon) == this.type)
            return 2;
        return super.damageMultiplier(isUser, context, events);
    }

}

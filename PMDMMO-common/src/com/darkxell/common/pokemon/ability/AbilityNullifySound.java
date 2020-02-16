package com.darkxell.common.pokemon.ability;

import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityNullifySound extends Ability {

    public AbilityNullifySound(int id) {
        super(id);
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveContext context, boolean isUser) {
        if (!isUser && context.move.isSound())
            return PokemonType.NO_EFFECT;
        return super.applyEffectivenessModifications(effectiveness, context, isUser);
    }

}

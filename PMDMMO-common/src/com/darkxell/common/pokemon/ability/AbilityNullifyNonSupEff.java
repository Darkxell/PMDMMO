package com.darkxell.common.pokemon.ability;

import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityNullifyNonSupEff extends Ability {

    public AbilityNullifyNonSupEff(int id) {
        super(id);
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveContext context, boolean isUser) {
        if (context.move.getType(context.user.usedPokemon) == PokemonType.Unknown)
            return effectiveness;
        if (!isUser && effectiveness < PokemonType.SUPER_EFFECTIVE)
            return PokemonType.NO_EFFECT;
        return super.applyEffectivenessModifications(effectiveness, context, isUser);
    }

}

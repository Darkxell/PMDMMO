package com.darkxell.common.pokemon.ability;

import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityNullifyType extends Ability {

    public final PokemonType type;

    public AbilityNullifyType(int id, PokemonType type) {
        super(id);
        this.type = type;
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveContext context, boolean isUser) {
        if (!isUser && context.move.getType(context.user.usedPokemon) == this.type)
            return PokemonType.NO_EFFECT;
        return super.applyEffectivenessModifications(effectiveness, context, isUser);
    }

}

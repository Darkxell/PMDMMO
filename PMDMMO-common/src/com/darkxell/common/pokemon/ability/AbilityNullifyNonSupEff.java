package com.darkxell.common.pokemon.ability;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityNullifyNonSupEff extends Ability {

    public AbilityNullifyNonSupEff(int id) {
        super(id);
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target,
            boolean isUser, Floor floor) {
        if (move.move.move().getType(move.user.usedPokemon) == PokemonType.Unknown)
            return effectiveness;
        if (!isUser && effectiveness < PokemonType.SUPER_EFFECTIVE)
            return PokemonType.NO_EFFECT;
        return super.applyEffectivenessModifications(effectiveness, move, target, isUser, floor);
    }

}

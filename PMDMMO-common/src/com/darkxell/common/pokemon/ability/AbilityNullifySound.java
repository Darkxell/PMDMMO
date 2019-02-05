package com.darkxell.common.pokemon.ability;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityNullifySound extends Ability {

    public AbilityNullifySound(int id) {
        super(id);
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target,
            boolean isUser, Floor floor) {
        if (!isUser && move.move.move().sound)
            return PokemonType.NO_EFFECT;
        return super.applyEffectivenessModifications(effectiveness, move, target, isUser, floor);
    }

}

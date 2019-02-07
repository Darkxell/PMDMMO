package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusCondition;

public class PreventsMovesStatusCondition extends StatusCondition {

    public PreventsMovesStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public boolean preventsUsingMove(LearnedMove move, DungeonPokemon pokemon, Floor floor) {
        return true;
    }

}

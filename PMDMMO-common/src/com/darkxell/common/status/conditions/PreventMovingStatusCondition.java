package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class PreventMovingStatusCondition extends StatusCondition {

    public PreventMovingStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public boolean preventsMoving(DungeonPokemon pokemon, Floor floor) {
        return true;
    }

}

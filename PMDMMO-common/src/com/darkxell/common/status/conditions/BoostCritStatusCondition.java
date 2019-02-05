package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class BoostCritStatusCondition extends StatusCondition {

    public final int critBoost;

    public BoostCritStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int critBoost) {
        super(id, isAilment, durationMin, durationMax);
        this.critBoost = critBoost;
    }

    @Override
    public int applyCriticalRateModifications(int critical, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<DungeonEvent> events) {
        if (critical < this.critBoost && critical != 0)
            return this.critBoost;
        return super.applyCriticalRateModifications(critical, move, target, isUser, floor, events);
    }

}

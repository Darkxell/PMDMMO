package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusCondition;

public class ParalyzedStatusCondition extends StatusCondition {

    public ParalyzedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<Event> events) {
        if (stat == Stat.Speed)
            return stage - 1;
        return super.applyStatStageModifications(stat, stage, move, target, isUser, floor, events);
    }

    @Override
    public boolean preventsUsingMove(LearnedMove move, DungeonPokemon pokemon, Floor floor) {
        return true;
    }

}

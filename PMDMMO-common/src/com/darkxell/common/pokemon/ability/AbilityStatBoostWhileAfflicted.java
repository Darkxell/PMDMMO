package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusConditions;

public class AbilityStatBoostWhileAfflicted extends AbilityStatBoost {
    public AbilityStatBoostWhileAfflicted(int id, Stat stat) {
        super(id, stat, 2);
    }

    @Override
    protected boolean shouldBoost(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<Event> events) {
        if (!super.shouldBoost(stat, value, move, target, isUser, floor, events))
            return false;
        return target.hasStatusCondition(StatusConditions.Poisoned)
                || target.hasStatusCondition(StatusConditions.Badly_poisoned)
                || target.hasStatusCondition(StatusConditions.Burn)
                || target.hasStatusCondition(StatusConditions.Paralyzed);
    }

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.status.StatusConditions;

public class AbilityStatBoostWhileAfflicted extends AbilityStatBoost {
    public AbilityStatBoostWhileAfflicted(int id, Stat stat) {
        super(id, stat, 2);
    }

    @Override
    protected boolean shouldBoost(Stat stat, double value, MoveContext context, boolean isUser, ArrayList<Event> events) {
        if (!super.shouldBoost(stat, value,context, isUser,  events))
            return false;
        return context.target.hasStatusCondition(StatusConditions.Poisoned)
                || context.target.hasStatusCondition(StatusConditions.Badly_poisoned)
                || context.target.hasStatusCondition(StatusConditions.Burn)
                || context.target.hasStatusCondition(StatusConditions.Paralyzed);
    }

}

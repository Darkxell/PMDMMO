package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.status.StatusCondition;

public class ImmuneStatusCondition extends StatusCondition {

    public ImmuneStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        if (stat == Stat.Accuracy && !isUser && context.target != context.user)
            return 0;
        return super.applyStatModifications(stat, value, context, isUser, events);
    }

}

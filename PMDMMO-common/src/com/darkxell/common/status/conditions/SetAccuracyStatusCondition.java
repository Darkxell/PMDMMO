package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.status.StatusCondition;

public class SetAccuracyStatusCondition extends StatusCondition {

    public final int accuracy;

    public SetAccuracyStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int accuracy) {
        super(id, isAilment, durationMin, durationMax);
        this.accuracy = accuracy;
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        if (isUser && context.user.hasStatusCondition(this))
            return this.accuracy;
        return super.applyStatModifications(stat, value, context, isUser, events);
    }

}

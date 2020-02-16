package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.status.StatusCondition;

public class ChangeAttackerStatStatusCondition extends StatusCondition {

    /** The multiplier to apply (default to 1 for no change). */
    public final double multiply;
    /** The stage change to apply (default to 0 for no change). */
    public final int stage;
    /** The stat to modify. */
    public final Stat stat;

    public ChangeAttackerStatStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, Stat stat,
            int stage, double multiply) {
        super(id, isAilment, durationMin, durationMax);
        this.stat = stat;
        this.stage = stage;
        this.multiply = multiply;
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        if (stat == this.stat && !isUser)
            return value * this.multiply;
        return super.applyStatModifications(stat, value, context, isUser, events);
    }

    @Override
    public int applyStatStageModifications(Stat stat, int stage, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        if (stat == this.stat && !isUser)
            return stage + this.stage;
        return super.applyStatStageModifications(stat, stage, context, isUser, events);
    }

}

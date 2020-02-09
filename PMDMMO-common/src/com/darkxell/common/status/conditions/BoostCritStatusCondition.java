package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.status.StatusCondition;

public class BoostCritStatusCondition extends StatusCondition {

    public final int critBoost;

    public BoostCritStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int critBoost) {
        super(id, isAilment, durationMin, durationMax);
        this.critBoost = critBoost;
    }

    @Override
    public int applyCriticalRateModifications(int critical, MoveContext context, boolean isUser, ArrayList<Event> events) {
        if (critical < this.critBoost && critical != 0)
            return this.critBoost;
        return super.applyCriticalRateModifications(critical, context, isUser, events);
    }

}

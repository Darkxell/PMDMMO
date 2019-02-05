package com.darkxell.common.status.conditions;

import com.darkxell.common.status.StatusCondition;

public abstract class PreventStatusCondition extends StatusCondition {

    public PreventStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    public abstract boolean prevents(StatusCondition condition);

}

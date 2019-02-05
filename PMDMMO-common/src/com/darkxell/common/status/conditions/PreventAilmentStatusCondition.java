package com.darkxell.common.status.conditions;

import com.darkxell.common.status.StatusCondition;

public class PreventAilmentStatusCondition extends PreventStatusCondition {

    public PreventAilmentStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public boolean prevents(StatusCondition condition) {
        return condition.isAilment;
    }

}

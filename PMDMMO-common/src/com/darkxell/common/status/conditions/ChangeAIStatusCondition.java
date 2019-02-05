package com.darkxell.common.status.conditions;

import com.darkxell.common.status.StatusCondition;

public class ChangeAIStatusCondition extends StatusCondition {

    public ChangeAIStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

}

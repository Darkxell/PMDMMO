package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.status.AppliedStatusCondition;

public class DoubleIfTargetAilmentEffect extends MoveEffect {

    public DoubleIfTargetAilmentEffect(int id) {
        super(id);
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        for (AppliedStatusCondition s : moveEvent.target.activeStatusConditions())
            if (s.condition.isAilment)
                return 2;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

}

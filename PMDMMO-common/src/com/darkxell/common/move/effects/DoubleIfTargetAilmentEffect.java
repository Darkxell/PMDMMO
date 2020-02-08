package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.status.AppliedStatusCondition;

public class DoubleIfTargetAilmentEffect extends MoveEffect {

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<Event> events) {
        for (AppliedStatusCondition s : moveEvent.target.activeStatusConditions())
            if (s.condition.isAilment) return 2;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

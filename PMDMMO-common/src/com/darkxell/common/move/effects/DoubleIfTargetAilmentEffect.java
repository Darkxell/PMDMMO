package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.status.AppliedStatusCondition;

public class DoubleIfTargetAilmentEffect extends MoveEffect {

    @Override
    public double damageMultiplier(boolean isUser, MoveContext context, ArrayList<Event> events) {
        if (context.target != null)
            for (AppliedStatusCondition s : context.target.activeStatusConditions())
                if (s.condition.isAilment)
                    return 2;
        return super.damageMultiplier(isUser, context, events);
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
    }

}

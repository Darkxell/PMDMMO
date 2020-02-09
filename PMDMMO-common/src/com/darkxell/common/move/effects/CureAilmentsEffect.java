package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.status.AppliedStatusCondition;

public class CureAilmentsEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            for (AppliedStatusCondition s : context.target.activeStatusConditions())
                if (s.condition.isAilment) {
                    effects.add(new StatusConditionEndedEvent(context.floor, context.event, s,
                            StatusConditionEndReason.HEALED));
                }
        }
    }

}

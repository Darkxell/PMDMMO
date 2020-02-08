package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrapStatusConditionEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            boolean willSucceed = !context.user.hasStatusCondition(StatusConditions.Wrapping)
                    && !context.target.hasStatusCondition(StatusConditions.Wrapped);

            if (willSucceed) {
                AppliedStatusCondition wrapped = StatusConditions.Wrapped.create(context.floor, context.target,
                        context);
                wrapped.addFlag("wrapper:" + context.user.id());

                AppliedStatusCondition wrapping = StatusConditions.Wrapping.create(context.floor, context.user,
                        context);
                wrapped.addFlag("wrapped:" + context.target.id());

                effects.add(new StatusConditionCreatedEvent(context.floor, context.event, wrapped));
                effects.add(new StatusConditionCreatedEvent(context.floor, context.event, wrapping));
            }
        }
    }

}

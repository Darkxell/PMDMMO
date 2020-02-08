package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrapStatusConditionEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            boolean willSucceed = !moveEvent.usedMove.user.hasStatusCondition(StatusConditions.Wrapping)
                    && !moveEvent.target.hasStatusCondition(StatusConditions.Wrapped);

            if (willSucceed) {
                AppliedStatusCondition wrapped = StatusConditions.Wrapped.create(moveEvent.floor, moveEvent.target,
                        moveEvent);
                wrapped.addFlag("wrapper:" + moveEvent.usedMove.user.id());

                AppliedStatusCondition wrapping = StatusConditions.Wrapping.create(moveEvent.floor,
                        moveEvent.usedMove.user, moveEvent);
                wrapped.addFlag("wrapped:" + moveEvent.target.id());

                effects.add(new StatusConditionCreatedEvent(moveEvent.floor, moveEvent, wrapped));
                effects.add(new StatusConditionCreatedEvent(moveEvent.floor, moveEvent, wrapping));
            }
        }
    }

}

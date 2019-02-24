package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrapStatusConditionEffect extends MoveEffect {

    public WrapStatusConditionEffect(int id) {
        super(id);
    }

    @Override
    protected void mainEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.mainEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            boolean willSucceed = !moveEvent.user.hasStatusCondition(StatusConditions.Wrapping)
                    && !target.hasStatusCondition(StatusConditions.Wrapped);

            if (willSucceed) {
                AppliedStatusCondition wrapped = StatusConditions.Wrapped.create(floor, target, moveEvent, floor.random);
                wrapped.addFlag("wrapper:" + moveEvent.user.id());

                AppliedStatusCondition wrapping = StatusConditions.Wrapping.create(floor, moveEvent.user, moveEvent,
                        floor.random);
                wrapped.addFlag("wrapped:" + target.id());

                effects.createEffect(new StatusConditionCreatedEvent(floor, eventSource, wrapped), moveEvent, missed, false, target);
                effects.createEffect(new StatusConditionCreatedEvent(floor, eventSource, wrapping), moveEvent, missed, false, moveEvent.user);
            }
        }
    }

}

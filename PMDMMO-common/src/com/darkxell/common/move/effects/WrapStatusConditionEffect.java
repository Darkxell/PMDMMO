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
            boolean willSucceed = !moveEvent.usedMove.user.hasStatusCondition(StatusConditions.Wrapping) && !moveEvent.target.hasStatusCondition(StatusConditions.Wrapped);

            if (willSucceed) {
                AppliedStatusCondition wrapped = StatusConditions.Wrapped.create(moveEvent.floor, moveEvent.target, moveEvent, moveEvent.floor.random);
                wrapped.addFlag("wrapper:" + moveEvent.usedMove.user.id());

                AppliedStatusCondition wrapping = StatusConditions.Wrapping.create(moveEvent.floor, moveEvent.usedMove.user, moveEvent, moveEvent.floor.random);
                wrapped.addFlag("wrapped:" + moveEvent.target.id());

                effects.createEffect(new StatusConditionCreatedEvent(moveEvent.floor, moveEvent, wrapped), moveEvent, missed, false, moveEvent.target);
                effects.createEffect(new StatusConditionCreatedEvent(moveEvent.floor, moveEvent, wrapping), moveEvent, missed, false, moveEvent.usedMove.user);
            }
        }
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.status.StatusCondition;

public class RemoveStatusConditionBeforeDamageEffect extends MoveEffect {

    public final StatusCondition condition;

    public RemoveStatusConditionBeforeDamageEffect(StatusCondition condition) {
        super(-1);
        this.condition = condition;
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && moveEvent.target.hasStatusCondition(this.condition) && createAdditionals) {
            effects.add(moveEvent.target.getStatusCondition(this.condition).finish(moveEvent.floor,
                    StatusConditionEndReason.BROKEN, moveEvent));
        }
    }

}

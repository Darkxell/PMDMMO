package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.status.StatusCondition;

public class RemoveStatusConditionBeforeDamageEffect extends MoveEffect {

    public final StatusCondition condition;

    public RemoveStatusConditionBeforeDamageEffect(int id, StatusCondition condition) {
        super(id);
        this.condition = condition;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed && moveEvent.target.hasStatusCondition(this.condition)) {
            effects.createEffect(moveEvent.target.getStatusCondition(this.condition).finish(moveEvent.floor,
                    StatusConditionEndReason.BROKEN, moveEvent), true, true);
        }
    }

}

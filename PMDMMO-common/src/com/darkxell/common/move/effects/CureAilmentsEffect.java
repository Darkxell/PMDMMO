package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.status.AppliedStatusCondition;

public class CureAilmentsEffect extends MoveEffect {

    public CureAilmentsEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed)
            for (AppliedStatusCondition s : target.activeStatusConditions())
                if (s.condition.isAilment)
                    effects.createEffect(new StatusConditionEndedEvent(floor, eventSource, s, StatusConditionEndReason.HEALED),
                            moveEvent, missed, false, target);
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class MultipleAttacksMissingStopsEffect extends MultipleAttacksEffect {

    public MultipleAttacksMissingStopsEffect(int id, int attacksMin, int attacksMax) {
        super(id, attacksMin, attacksMax);
    }

    @Override
    protected String descriptionID() {
        return super.descriptionID() + "_missingstops";
    }

    @Override
    protected boolean shouldContinue(int attacksleft, MoveUseEvent moveEvent, MoveEffectCalculator calculator,
            boolean missed, MoveEvents effects) {
        return super.shouldContinue(attacksleft, moveEvent, calculator, missed, effects) && !missed;
    }

}

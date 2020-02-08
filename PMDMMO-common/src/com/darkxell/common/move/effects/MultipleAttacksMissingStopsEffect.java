package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class MultipleAttacksMissingStopsEffect extends MultipleAttacksEffect {

    public MultipleAttacksMissingStopsEffect(int attacksMin, int attacksMax) {
        super(attacksMin, attacksMax);
    }

    @Override
    protected String descriptionID() {
        return super.descriptionID() + "_missingstops";
    }

    @Override
    protected boolean shouldContinue(int attacksleft, MoveUseEvent moveEvent, MoveEffectCalculator calculator,
            boolean missed, ArrayList<Event> effects) {
        return super.shouldContinue(attacksleft, moveEvent, calculator, missed, effects) && !missed;
    }

}

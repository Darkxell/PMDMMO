package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;

public class MultipleAttacksMissingStopsEffect extends MultipleAttacksEffect {

    public MultipleAttacksMissingStopsEffect(int attacksMin, int attacksMax) {
        super(attacksMin, attacksMax);
    }

    @Override
    protected String descriptionID() {
        return super.descriptionID() + "_missingstops";
    }

    @Override
    protected boolean shouldContinue(int attacksleft, MoveContext context, MoveEffectCalculator calculator,
            boolean missed, ArrayList<Event> effects) {
        return super.shouldContinue(attacksleft, context, calculator, missed, effects) && !missed;
    }

}

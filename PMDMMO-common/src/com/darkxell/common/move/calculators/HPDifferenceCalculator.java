package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HPDifferenceCalculator extends MoveEffectCalculator {

    public HPDifferenceCalculator(MoveContext context) {
        super(context);
    }

    @Override
    public int compute(ArrayList<Event> events) {
        int diff = context.target.getHp() - context.user.getHp();
        return diff < 0 ? 0 : diff;
    }

}

package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HalfTargetHPDamageCalculator extends MoveEffectCalculator {

    public HalfTargetHPDamageCalculator(MoveContext context) {
        super(context);
    }

    @Override
    public int compute(ArrayList<Event> events) {
        int hp = context.target.getHp();
        if (hp % 2 == 1)
            hp -= 1;
        return hp / 2;
    }

}

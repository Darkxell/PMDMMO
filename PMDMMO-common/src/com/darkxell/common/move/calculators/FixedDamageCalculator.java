package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class FixedDamageCalculator extends MoveEffectCalculator {

    public final int damage;

    public FixedDamageCalculator(MoveContext context, int damage) {
        super(context);
        this.damage = damage;
    }

    @Override
    public int compute(ArrayList<Event> events) {
        if (this.effectiveness() == 0)
            return 0;
        return this.damage;
    }

}

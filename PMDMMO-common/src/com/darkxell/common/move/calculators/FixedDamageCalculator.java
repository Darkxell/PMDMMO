package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class FixedDamageCalculator extends MoveEffectCalculator {

    public final int damage;

    public FixedDamageCalculator(MoveUseEvent moveEvent, int damage) {
        super(moveEvent);
        this.damage = damage;
    }

    @Override
    public int compute(ArrayList<Event> events) {
        if (this.effectiveness() == 0)
            return 0;
        return this.damage;
    }

}

package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;

public class FixedDamageCalculator extends MoveEffectCalculator {

    public final int damage;

    public FixedDamageCalculator(MoveUseEvent moveEvent, int damage) {
        super(moveEvent);
        this.damage = damage;
    }

    @Override
    public int compute(ArrayList<Event> events) {
        return this.damage;
    }

}

package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class MaxHpMultiplierDamageCalculator extends MoveEffectCalculator {

    public final double multiplier;

    public MaxHpMultiplierDamageCalculator(MoveUseEvent moveEvent, double multiplier) {
        super(moveEvent);
        this.multiplier = multiplier;
    }
    
    @Override
    public int compute(ArrayList<Event> events) {
        return (int) Math.round(this.target.getMaxHP() * this.multiplier);
    }

}

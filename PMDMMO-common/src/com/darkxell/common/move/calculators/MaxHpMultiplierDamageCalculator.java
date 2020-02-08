package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class MaxHpMultiplierDamageCalculator extends MoveEffectCalculator {

    public final double multiplier;

    public MaxHpMultiplierDamageCalculator(MoveContext context, double multiplier) {
        super(context);
        this.multiplier = multiplier;
    }
    
    @Override
    public int compute(ArrayList<Event> events) {
        return (int) Math.round(context.target.getMaxHP() * this.multiplier);
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculators.MaxHpMultiplierDamageCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class DealMaxHpMultiplierDamageEffect extends MoveEffect {

    public final double multiplier;

    public DealMaxHpMultiplierDamageEffect(double multiplier) {
        this.multiplier = multiplier;
    }
    
    @Override
    public MoveEffectCalculator buildCalculator(MoveContext context) {
        return new MaxHpMultiplierDamageCalculator(context, this.multiplier);
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

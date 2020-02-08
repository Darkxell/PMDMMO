package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.calculators.MaxHpMultiplierDamageCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class DealMaxHpMultiplierDamageEffect extends MoveEffect {

    public final double multiplier;

    public DealMaxHpMultiplierDamageEffect(double multiplier) {
        this.multiplier = multiplier;
    }
    
    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new MaxHpMultiplierDamageCalculator(moveEvent, this.multiplier);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.MaxHpMultiplierDamageCalculator;

public class DealMaxHpMultiplierDamageEffect extends MoveEffect {

    public final double multiplier;

    public DealMaxHpMultiplierDamageEffect(int id, double multiplier) {
        super(id);
        this.multiplier = multiplier;
    }
    
    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new MaxHpMultiplierDamageCalculator(moveEvent, this.multiplier);
    }

}

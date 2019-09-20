package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.FixedDamageCalculator;

public class FixedDamageEffect extends MoveEffect {

    public final int damage;

    public FixedDamageEffect(int id, int damage) {
        super(id);
        this.damage = damage;
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new FixedDamageCalculator(moveEvent, this.damage);
    }

}

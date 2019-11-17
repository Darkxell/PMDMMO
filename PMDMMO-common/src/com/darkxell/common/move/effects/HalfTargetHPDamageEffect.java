package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.HalfTargetHPDamageCalculator;

public class HalfTargetHPDamageEffect extends MoveEffect {

    public HalfTargetHPDamageEffect(int id) {
        super(id);
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new HalfTargetHPDamageCalculator(moveEvent);
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.UserLevelDamageCalculator;

public class UserLevelDamageEffect extends MoveEffect {

    public UserLevelDamageEffect(int id) {
        super(id);
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new UserLevelDamageCalculator(moveEvent);
    }

}

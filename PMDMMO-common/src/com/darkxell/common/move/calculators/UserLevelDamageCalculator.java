package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class UserLevelDamageCalculator extends MoveEffectCalculator {

    public UserLevelDamageCalculator(MoveContext context) {
        super(context);
    }

    @Override
    public int compute(ArrayList<Event> events) {
        return context.user.level();
    }

}

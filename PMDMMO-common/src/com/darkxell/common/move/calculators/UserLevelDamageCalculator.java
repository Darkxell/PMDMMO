package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;

public class UserLevelDamageCalculator extends MoveEffectCalculator {

    public UserLevelDamageCalculator(MoveUseEvent moveEvent) {
        super(moveEvent);
    }

    @Override
    public int compute(ArrayList<Event> events) {
        return move.user.level();
    }

}

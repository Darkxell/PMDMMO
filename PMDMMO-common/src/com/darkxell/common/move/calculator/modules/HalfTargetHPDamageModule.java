package com.darkxell.common.move.calculator.modules;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.CalculatorDamageModule;
import com.darkxell.common.move.calculator.MoveEffectCalculator;

public class HalfTargetHPDamageModule implements CalculatorDamageModule {

    @Override
    public int compute(MoveContext context, MoveEffectCalculator calculator, ArrayList<Event> events) {
        int hp = context.target.getHp();
        if (hp % 2 == 1)
            hp -= 1;
        return hp / 2;
    }

}

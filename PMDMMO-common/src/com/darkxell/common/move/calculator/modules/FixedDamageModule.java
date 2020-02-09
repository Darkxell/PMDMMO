package com.darkxell.common.move.calculator.modules;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.CalculatorDamageModule;
import com.darkxell.common.move.calculator.MoveEffectCalculator;

public class FixedDamageModule implements CalculatorDamageModule {

    public final int damage;

    public FixedDamageModule(int damage) {
        this.damage = damage;
    }

    @Override
    public int compute(MoveContext context, MoveEffectCalculator calculator, ArrayList<Event> events) {
        if (calculator.effectiveness() == 0)
            return 0;
        return this.damage;
    }

}

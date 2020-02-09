package com.darkxell.common.move.calculator.modules;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.CalculatorMoveLandingModule;
import com.darkxell.common.move.calculator.MoveEffectCalculator;

public class CantMissMoveLandingModule implements CalculatorMoveLandingModule {
    
    @Override
    public boolean misses(MoveContext context, MoveEffectCalculator calculator, ArrayList<Event> events) {
        return false;
    }

}

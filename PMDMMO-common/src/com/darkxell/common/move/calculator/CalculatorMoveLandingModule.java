package com.darkxell.common.move.calculator;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;

public interface CalculatorMoveLandingModule {

    public static class DefaultMoveLandingModule implements CalculatorMoveLandingModule {
    }

    /**
     * Determines whether the move lands or not.
     * 
     * @param  usedMove - The Move used.
     * @param  target   - The Pokemon receiving the Move.
     * @param  floor    - The Floor context.
     * @return          True if this Move misses.
     */
    public default boolean misses(MoveContext context, MoveEffectCalculator calculator, ArrayList<Event> events) {
        if (context.target == null)
            return false;

        int accuracy = context.move.getAccuracy();

        double userAccuracy = calculator.accuracyStat(events);
        double evasion = calculator.evasionStat(events);

        accuracy = (int) (accuracy * userAccuracy * evasion);

        return context.floor.random.nextDouble() * 100 > accuracy; // ITS SUPERIOR because you return 'MISSES'
    }

}

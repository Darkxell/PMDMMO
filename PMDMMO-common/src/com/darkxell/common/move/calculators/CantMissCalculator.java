package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class CantMissCalculator extends MoveEffectCalculator {

    public CantMissCalculator(MoveContext context) {
        super(context);
    }

    @Override
    public boolean misses(ArrayList<Event> events) {
        return false;
    }

}

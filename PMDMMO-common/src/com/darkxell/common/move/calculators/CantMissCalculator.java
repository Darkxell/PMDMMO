package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class CantMissCalculator extends MoveEffectCalculator {

    public CantMissCalculator(MoveUseEvent moveEvent) {
        super(moveEvent);
    }

    @Override
    public boolean misses(ArrayList<Event> events) {
        return false;
    }

}

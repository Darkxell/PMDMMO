package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;

public class CantMissCalculator extends MoveEffectCalculator {

    public CantMissCalculator(MoveUseEvent moveEvent) {
        super(moveEvent);
    }

    @Override
    public boolean misses(ArrayList<DungeonEvent> events) {
        return false;
    }

}

package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;

public class HPDifferenceCalculator extends MoveEffectCalculator {

    public HPDifferenceCalculator(MoveUseEvent moveEvent) {
        super(moveEvent);
    }

    @Override
    public int compute(ArrayList<DungeonEvent> events) {
        int diff = this.target.getHp() - this.user().getHp();
        return diff < 0 ? 0 : diff;
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class MultiplyWeightEffect extends MoveEffect {

    private final int[][] multiplierTable = new int[][] { { 0, 20, 30, 40, 50, 60, 70, 80, 200 },
            { 60, 70, 80, 90, 100, 110, 120, 130, 140 } };

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<Event> events) {
        float weight = moveEvent.target.species().weight;
        for (int i = this.multiplierTable.length - 1; i >= 0; --i)
            if (weight >= this.multiplierTable[i][0])
                return this.multiplierTable[i][1] * 1. / 100;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {
    }

}

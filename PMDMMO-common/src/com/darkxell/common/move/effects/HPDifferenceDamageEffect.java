package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.calculators.HPDifferenceCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HPDifferenceDamageEffect extends MoveEffect {

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new HPDifferenceCalculator(moveEvent);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

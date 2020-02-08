package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculators.HPDifferenceCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HPDifferenceDamageEffect extends MoveEffect {

    @Override
    public MoveEffectCalculator buildCalculator(MoveContext context) {
        return new HPDifferenceCalculator(context);
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

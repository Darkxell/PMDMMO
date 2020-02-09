package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.calculator.modules.CantMissMoveLandingModule;
import com.darkxell.common.move.effect.MoveEffect;

public class CantMissEffect extends MoveEffect {

    @Override
    public void buildCalculator(MoveContext context, MoveEffectCalculator calculator) {
        calculator.setMoveLandingModule(new CantMissMoveLandingModule());
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.calculator.modules.HalfTargetHPDamageModule;
import com.darkxell.common.move.effect.MoveEffect;

public class HalfTargetHPDamageEffect extends MoveEffect {

    @Override
    public void buildCalculator(MoveContext context, MoveEffectCalculator calculator) {
        calculator.setDamageModule(new HalfTargetHPDamageModule());
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {}

}

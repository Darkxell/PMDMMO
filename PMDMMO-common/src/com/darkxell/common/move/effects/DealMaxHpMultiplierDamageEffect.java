package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.calculator.modules.MaxHpMultiplierDamageModule;
import com.darkxell.common.move.effect.MoveEffect;

public class DealMaxHpMultiplierDamageEffect extends MoveEffect {

    public final double multiplier;

    public DealMaxHpMultiplierDamageEffect(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void buildCalculator(MoveContext context, MoveEffectCalculator calculator) {
        calculator.setDamageModule(new MaxHpMultiplierDamageModule(this.multiplier));
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {}

}

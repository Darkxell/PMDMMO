package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.calculator.modules.FixedDamageModule;
import com.darkxell.common.move.effect.MoveEffect;

public class FixedDamageEffect extends MoveEffect {

    public final int damage;

    public FixedDamageEffect(int damage) {
        this.damage = damage;
    }

    @Override
    public void buildCalculator(MoveContext context, MoveEffectCalculator calculator) {
        calculator.setDamageModule(new FixedDamageModule(this.damage));
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {}

}

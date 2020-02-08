package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculators.FixedDamageCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class FixedDamageEffect extends MoveEffect {

    public final int damage;

    public FixedDamageEffect(int damage) {
        this.damage = damage;
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveContext context) {
        return new FixedDamageCalculator(context, this.damage);
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

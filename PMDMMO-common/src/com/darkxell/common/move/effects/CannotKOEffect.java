package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class CannotKOEffect extends MoveEffect {

    @Override
    public double applyDamageModifications(double damage, boolean isUser, MoveContext context,
            ArrayList<Event> events) {
        if (damage >= context.target.getHp())
            return context.target.getHp() - 1;
        return super.applyDamageModifications(damage, isUser, context, events);
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
    }

}

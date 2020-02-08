package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HealEffect extends MoveEffect {

    public final double percentage;

    public HealEffect(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && createAdditionals) {
            int health = (int) Math.round(context.target.getMaxHP() * this.percentage);
            effects.add(new HealthRestoredEvent(context.floor, context.event, context.target, health));
        }
    }

}

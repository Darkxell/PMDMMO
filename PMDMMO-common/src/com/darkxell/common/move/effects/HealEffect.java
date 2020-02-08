package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HealEffect extends MoveEffect {

    public final double percentage;

    public HealEffect(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && createAdditionals) {
            int health = (int) Math.round(moveEvent.target.getMaxHP() * this.percentage);
            effects.add(new HealthRestoredEvent(moveEvent.floor, moveEvent, moveEvent.target, health));
        }
    }

}

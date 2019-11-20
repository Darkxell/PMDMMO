package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class HealEffect extends MoveEffect {

    public final double percentage;

    public HealEffect(int id, double percentage) {
        super(id);
        this.percentage = percentage;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            int health = (int) Math.round(moveEvent.target.getMaxHP() * this.percentage);
            effects.createEffect(new HealthRestoredEvent(moveEvent.floor, moveEvent, moveEvent.target, health),
                    moveEvent, missed, true);
        }
    }

}

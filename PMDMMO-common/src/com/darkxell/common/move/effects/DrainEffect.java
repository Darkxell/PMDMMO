package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.language.Message;

public class DrainEffect extends MoveEffect {

    public final int percent;

    public DrainEffect(int percent) {
        this.percent = percent;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && createAdditionals) {
            DamageDealtEvent damage = null;
            for (Event e : effects)
                if (e instanceof DamageDealtEvent) {
                    damage = (DamageDealtEvent) e;
                    break;
                }
            if (damage != null)
                effects.add(new HealthRestoredEvent(context.floor, context.event, context.user,
                        damage.damage * this.percent / 100));
        }
    }

    @Override
    public Message description() {
        return new Message("move.info.drain").addReplacement("<percent>", String.valueOf(this.percent));
    }

}

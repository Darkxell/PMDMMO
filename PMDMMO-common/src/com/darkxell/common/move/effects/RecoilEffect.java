package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.language.Message;

public class RecoilEffect extends MoveEffect {

    public final double percentage;

    public RecoilEffect(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && createAdditionals) {
            int damage = -1;
            for (Event e : effects)
                if (e instanceof DamageDealtEvent) {
                    DamageDealtEvent d = (DamageDealtEvent) e;
                    if (d.target == context.target && d.source == context)
                        damage = d.damage;
                }
            damage *= this.percentage / 100;
            effects.add(new DamageDealtEvent(context.floor, context.event, context.user, context.event.usedMove,
                    DamageType.RECOIL, damage));
        }
    }

    @Override
    public Message description() {
        return new Message("move.info.recoil").addReplacement("<percent>", String.valueOf(this.percentage));
    }

}

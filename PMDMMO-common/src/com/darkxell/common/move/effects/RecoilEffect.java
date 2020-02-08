package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.language.Message;

public class RecoilEffect extends MoveEffect {

    public final double percentage;

    public RecoilEffect(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {
        if (!missed && createAdditionals) {
            int damage = -1;
            for (Event e : effects)
                if (e instanceof DamageDealtEvent) {
                    DamageDealtEvent d = (DamageDealtEvent) e;
                    if (d.target == moveEvent.target && d.source == moveEvent)
                        damage = d.damage;
                }
            damage *= this.percentage / 100;
            effects.add(new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user, moveEvent.usedMove,
                    DamageType.RECOIL, damage));
        }
    }

    @Override
    public Message description() {
        return new Message("move.info.recoil").addReplacement("<percent>", String.valueOf(this.percentage));
    }

}

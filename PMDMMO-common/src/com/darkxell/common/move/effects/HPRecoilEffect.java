package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.util.language.Message;

public class HPRecoilEffect extends MoveEffect {

    public final double percent;

    public HPRecoilEffect(double percent) {
        this.percent = percent;
    }

    @Override
    public Message description() {
        return new Message("move.info.recoil_hp").addReplacement("<percent>", String.valueOf(this.percent));
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && createAdditionals) {
            int damage = context.user.getMaxHP();
            damage *= this.percent / 100;
            effects.add(new DamageDealtEvent(context.floor, context.event, context.user, context.event.usedMove,
                    DamageType.RECOIL, damage));
        }
    }

}

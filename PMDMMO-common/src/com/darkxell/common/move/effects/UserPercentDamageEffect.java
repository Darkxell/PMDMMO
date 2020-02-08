package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.language.Message;

public class UserPercentDamageEffect extends MoveEffect {

    public final double percent;

    public UserPercentDamageEffect(double percent) {
        this.percent = percent;
    }

    @Override
    public Message description() {
        String id = "move.info.damage_user_percent";
        return new Message(id).addReplacement("<percent>", String.valueOf(this.percent * 100));
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            Event event = new DamageDealtEvent(context.floor, context.event, context.user, context.event.usedMove,
                    DamageType.MOVE, (int) (context.user.getMaxHP() * this.percent));
            effects.add(event);
        }
    }

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.MoveContext;

public class AbilityPreventOneShot extends Ability {

    public AbilityPreventOneShot(int id) {
        super(id);
    }

    @Override
    public double applyDamageModifications(double damage, boolean isUser, MoveContext context,
            ArrayList<Event> events) {
        if (context.target.ability() == this && context.target.getHp() >= context.target.getMaxHP()
                && damage >= context.target.getHp()) {
            events.add(new TriggeredAbilityEvent(context.floor, context.event, context.target));
            return context.target.getHp() - 1;
        }
        return super.applyDamageModifications(damage, isUser, context, events);
    }

}

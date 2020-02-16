package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.MoveCategory;
import com.darkxell.common.move.MoveContext;

public class AbilityMultiplyIncomingDamage extends Ability {

    public final MoveCategory category;
    public final double multiplier;

    public AbilityMultiplyIncomingDamage(int id, MoveCategory category, double multiplier) {
        super(id);
        this.category = category;
        this.multiplier = multiplier;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveContext context, ArrayList<Event> events) {
        if (context.target.ability() == this && context.move.getCategory() == this.category) {
            events.add(new TriggeredAbilityEvent(context.floor, context.event, context.target));
            return this.multiplier;
        }
        return super.damageMultiplier(isUser, context, events);
    }

}

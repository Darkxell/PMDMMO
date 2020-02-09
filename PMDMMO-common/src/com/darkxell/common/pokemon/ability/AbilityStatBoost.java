package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class AbilityStatBoost extends Ability {

    public final double multiplier;
    public final Stat stat;

    public AbilityStatBoost(int id, Stat stat, double multiplier) {
        super(id);
        this.stat = stat;
        this.multiplier = multiplier;
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        if (this.shouldBoost(stat, value, context, isUser, events)) {
            events.add(new TriggeredAbilityEvent(context.floor, context.event, context.user));
            return value * this.multiplier;
        }
        return super.applyStatModifications(stat, value, context, isUser, events);
    }

    protected boolean shouldBoost(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        return stat == this.stat && isUser;
    }

}

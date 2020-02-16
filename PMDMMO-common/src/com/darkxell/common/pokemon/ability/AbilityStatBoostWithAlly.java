package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityStatBoostWithAlly extends AbilityStatBoost {

    Ability allyAbility;

    public AbilityStatBoostWithAlly(int id, Stat stat, double multiplier) {
        super(id, stat, multiplier);
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        if (stat == this.stat && isUser) {
            boolean found = false;
            for (DungeonPokemon p : context.floor.listPokemon())
                if (p.ability() == this.allyAbility && p.isAlliedWith(p)) {
                    found = true;
                    break;
                }
            if (found) {
                events.add(new TriggeredAbilityEvent(context.floor, context.event, context.user));
                return value * this.multiplier;
            }
        }
        return super.applyStatModifications(stat, value, context, isUser, events);
    }

    @Override
    protected boolean shouldBoost(Stat stat, double value, MoveContext context, boolean isUser,
            ArrayList<Event> events) {
        for (DungeonPokemon p : context.floor.listPokemon())
            if (p.ability() == this.allyAbility && p.isAlliedWith(p))
                return super.shouldBoost(stat, value, context, isUser, events);
        return false;
    }

}

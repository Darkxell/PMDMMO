package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusConditions;

public class AbilityRunaway extends Ability {

    public AbilityRunaway(int id) {
        super(id);
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);
        if (event instanceof DamageDealtEvent) {
            DungeonPokemon p = ((DamageDealtEvent) event).target;
            if (p != concerned)
                return;
            if (p.ability() == this && p.getHpPercentage() < .5 && !p.hasStatusCondition(StatusConditions.Terrified)
                    && !p.isTeamLeader()) {
                TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, event, p);
                resultingEvents.add(abilityevent);
                resultingEvents.add(new StatusConditionCreatedEvent(floor, abilityevent,
                        StatusConditions.Terrified.create(floor, p, this, floor.random)));
            }
        }
    }

}

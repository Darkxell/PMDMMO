package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class AbilityPreventStatus extends Ability {

    private final StatusCondition[] conditions;

    public AbilityPreventStatus(int id, StatusCondition... conditions) {
        super(id);
        this.conditions = conditions;
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof StatusConditionCreatedEvent) {
            StatusConditionCreatedEvent e = (StatusConditionCreatedEvent) event;
            if (e.condition.pokemon != concerned) return;
            for (int i = 0; i < this.conditions.length; ++i)
                if (e.condition.condition == this.conditions[i]) {
                    e.consume();
                    resultingEvents.add(new TriggeredAbilityEvent(floor, event, concerned, this.conditions.length > 1 ? i + 1 : 0));
                }
        }
    }

    @Override
    public void onTurnStart(Floor floor, DungeonPokemon pokemon, ArrayList<Event> events) {
        super.onTurnStart(floor, pokemon, events);

        for (int i = 0; i < this.conditions.length; ++i)
            if (pokemon.hasStatusCondition(this.conditions[i])) {
                TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, DungeonEventSource.TRIGGER, pokemon,
                        i + this.conditions.length + 1);
                events.add(abilityevent);
                pokemon.getStatusCondition(this.conditions[i]).finish(floor, StatusConditionEndReason.PREVENTED, abilityevent, events);
            }
    }

}

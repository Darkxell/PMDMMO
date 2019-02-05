package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
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
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof StatusConditionCreatedEvent) {
            StatusConditionCreatedEvent e = (StatusConditionCreatedEvent) event;
            if (e.condition.pokemon != concerned)
                return;
            for (int i = 0; i < this.conditions.length; ++i)
                if (e.condition.condition == this.conditions[i]) {
                    e.consume();
                    resultingEvents
                            .add(new TriggeredAbilityEvent(floor, concerned, this.conditions.length > 1 ? i + 1 : 0));
                }
        }
    }

    @Override
    public void onTurnStart(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
        super.onTurnStart(floor, pokemon, events);

        for (int i = 0; i < this.conditions.length; ++i)
            if (pokemon.hasStatusCondition(this.conditions[i])) {
                events.add(new TriggeredAbilityEvent(floor, pokemon, i + this.conditions.length + 1));
                pokemon.getStatusCondition(this.conditions[i]).finish(floor, StatusConditionEndReason.PREVENTED,
                        events);
            }
    }

}

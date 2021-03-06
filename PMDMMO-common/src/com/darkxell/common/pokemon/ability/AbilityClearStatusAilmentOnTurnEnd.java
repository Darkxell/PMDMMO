package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;

public class AbilityClearStatusAilmentOnTurnEnd extends Ability {

    public final int chance;

    public AbilityClearStatusAilmentOnTurnEnd(int id, int chance) {
        super(id);
        this.chance = chance;
    }

    @Override
    public void onTurnStart(Floor floor, DungeonPokemon pokemon, ArrayList<Event> events) {
        super.onTurnStart(floor, pokemon, events);

        if (pokemon.hasStatusCondition(null)) {
            boolean triggers = floor.random.nextDouble() * 100 < this.chance;
            if (triggers) {
                TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, BaseEventSource.TRIGGER,
                        pokemon);
                events.add(abilityevent);
                for (AppliedStatusCondition condition : pokemon.activeStatusConditions())
                    events.add(condition.finish(floor, StatusConditionEndReason.HEALED, abilityevent));
            }
        }

    }

}

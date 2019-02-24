package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.StatusCondition;

public class CancelsUsingMovesOrbsStatusCondition extends StatusCondition {

    public CancelsUsingMovesOrbsStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (Ability.TRUANT.shouldTruant(floor, event, concerned, true)) {
            event.consume();
            TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, event, concerned);
            resultingEvents.add(abilityevent);
            resultingEvents.add(new TurnSkippedEvent(floor, abilityevent, concerned));
        }
    }

}

package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.DungeonPokemon;

public class PetrifiedStatusCondition extends PreventActionStatusCondition {

    public PetrifiedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);

        if (event instanceof MoveUseEvent && ((MoveUseEvent) event).target == concerned)
            resultingEvents
                    .add(concerned.getStatusCondition(this).finish(floor, StatusConditionEndReason.BROKEN, event));
    }

}

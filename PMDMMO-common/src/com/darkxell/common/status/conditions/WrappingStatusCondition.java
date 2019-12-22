package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrappingStatusCondition extends StatusCondition {

    public WrappingStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onEnd(StatusConditionEndedEvent event, ArrayList<Event> events) {
        super.onEnd(event, events);

        String wrappedid = null;
        for (String flag : event.condition.listFlags())
            if (flag.startsWith("wrapped:"))
                wrappedid = flag.substring("wrapped:".length());

        if (wrappedid != null && wrappedid.matches("-?\\d+")) {
            long id = Integer.parseInt(wrappedid);
            DungeonPokemon wrapped = event.floor.findPokemon(id);
            if (wrapped != null && wrapped.hasStatusCondition(StatusConditions.Wrapped))
                events.add(
                        wrapped.getStatusCondition(StatusConditions.Wrapped).finish(event.floor, event.reason, event));
        }
    }

}

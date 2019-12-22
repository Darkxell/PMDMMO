package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusConditions;

public class WrappedStatusCondition extends PeriodicDamageStatusCondition {

    public WrappedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int period, int damage) {
        super(id, isAilment, durationMin, durationMax, period, damage);
    }

    @Override
    public void onEnd(StatusConditionEndedEvent event, ArrayList<Event> events) {
        super.onEnd(event, events);

        String wrapperid = null;
        for (String flag : event.condition.listFlags())
            if (flag.startsWith("wrapper:"))
                wrapperid = flag.substring("wrapper:".length());

        if (wrapperid != null && wrapperid.matches("-?\\d+")) {
            long id = Integer.parseInt(wrapperid);
            DungeonPokemon wrapper = event.floor.findPokemon(id);
            if (wrapper != null && wrapper.hasStatusCondition(StatusConditions.Wrapping))
                events.add(
                        wrapper.getStatusCondition(StatusConditions.Wrapping).finish(event.floor, event.reason, event));
        }
    }

}

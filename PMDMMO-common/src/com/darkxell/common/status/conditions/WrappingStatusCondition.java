package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrappingStatusCondition extends StatusCondition {

    public WrappingStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onEnd(Floor floor, AppliedStatusCondition instance, StatusConditionEndReason reason,
            ArrayList<DungeonEvent> events) {
        super.onEnd(floor, instance, reason, events);

        String wrappedid = null;
        for (String flag : instance.listFlags())
            if (flag.startsWith("wrapped:"))
                wrappedid = flag.substring("wrapped:".length());

        if (wrappedid != null && wrappedid.matches("-?\\d+")) {
            long id = Integer.parseInt(wrappedid);
            DungeonPokemon wrapped = floor.findPokemon(id);
            if (wrapped != null && wrapped.hasStatusCondition(StatusConditions.Wrapped))
                wrapped.getStatusCondition(StatusConditions.Wrapped).finish(floor, reason, events);
        }
    }

}

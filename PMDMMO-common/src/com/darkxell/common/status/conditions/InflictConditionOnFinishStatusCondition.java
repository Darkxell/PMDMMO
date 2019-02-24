package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;

public class InflictConditionOnFinishStatusCondition extends StatusCondition {

    public final StatusCondition inflictedOnFinish;

    public InflictConditionOnFinishStatusCondition(int id, boolean isAilment, int durationMin, int durationMax,
            StatusCondition inflictedOnFinish) {
        super(id, isAilment, durationMin, durationMax);
        this.inflictedOnFinish = inflictedOnFinish;
    }

    @Override
    public void onEnd(Floor floor, AppliedStatusCondition instance, StatusConditionEndReason reason,
            ArrayList<DungeonEvent> events) {
        super.onEnd(floor, instance, reason, events);

        if (reason == StatusConditionEndReason.FINISHED)
            events.add(new StatusConditionCreatedEvent(floor,
                    eventSource, this.inflictedOnFinish.create(floor, instance.pokemon, this, floor.random)));
    }

}

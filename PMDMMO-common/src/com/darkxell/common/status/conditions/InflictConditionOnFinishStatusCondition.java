package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.status.StatusCondition;

public class InflictConditionOnFinishStatusCondition extends StatusCondition {

    public final StatusCondition inflictedOnFinish;

    public InflictConditionOnFinishStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, StatusCondition inflictedOnFinish) {
        super(id, isAilment, durationMin, durationMax);
        this.inflictedOnFinish = inflictedOnFinish;
    }

    @Override
    public void onEnd(StatusConditionEndedEvent event, ArrayList<DungeonEvent> events) {
        super.onEnd(event, events);

        if (event.reason == StatusConditionEndReason.FINISHED) events.add(new StatusConditionCreatedEvent(event.floor, event,
                this.inflictedOnFinish.create(event.floor, event.condition.pokemon, this, event.floor.random)));
    }

}

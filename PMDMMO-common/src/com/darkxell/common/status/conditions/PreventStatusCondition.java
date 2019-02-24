package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public abstract class PreventStatusCondition extends StatusCondition {

    public PreventStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof StatusConditionCreatedEvent) {
            StatusConditionCreatedEvent c = (StatusConditionCreatedEvent) event;
            if (c.condition.pokemon == concerned && this.prevents(c.condition.condition)) {
                c.consume();
                resultingEvents.add(new MessageEvent(floor,
                        eventSource, new Message("status.prevented.condition").addReplacement("<pokemon>", concerned.getNickname())
                                .addReplacement("<prevented>", this.name())
                                .addReplacement("<preventer>", c.condition.condition.name())));
            }
        }
    }

    public abstract boolean prevents(StatusCondition condition);

}

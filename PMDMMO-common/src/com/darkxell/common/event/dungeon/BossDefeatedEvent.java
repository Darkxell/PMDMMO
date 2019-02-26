package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;

public class BossDefeatedEvent extends Event {

    public BossDefeatedEvent(Floor floor, DungeonEventSource eventSource) {
        super(floor, eventSource);
        this.priority = PRIORITY_TURN_END;
    }

    @Override
    public String loggerMessage() {
        return "The boss has been defeated.";
    }

}

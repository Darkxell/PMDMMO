package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;

public class BossDefeatedEvent extends DungeonEvent {

    public BossDefeatedEvent(Floor floor) {
        super(floor, eventSource);
        this.priority = PRIORITY_TURN_END;
    }

    @Override
    public String loggerMessage() {
        return "The boss has been defeated.";
    }

}

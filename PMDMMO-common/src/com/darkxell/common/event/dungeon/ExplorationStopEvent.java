package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;

public class ExplorationStopEvent extends DungeonEvent {

    public final DungeonOutcome outcome;

    public ExplorationStopEvent(Floor floor, DungeonEventSource eventSource, DungeonOutcome outcome) {
        super(floor, eventSource);
        this.outcome = outcome;
    }

    @Override
    public String loggerMessage() {
        return "Dungeon exploration stopped.";
    }

}

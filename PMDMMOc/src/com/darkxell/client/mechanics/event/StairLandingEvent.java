package com.darkxell.client.mechanics.event;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.common.event.DungeonEvent;

public class StairLandingEvent extends DungeonEvent {

    public StairLandingEvent() {
        super(Persistence.floor, eventSource);
    }

    @Override
    public String loggerMessage() {
        return null;
    }

}

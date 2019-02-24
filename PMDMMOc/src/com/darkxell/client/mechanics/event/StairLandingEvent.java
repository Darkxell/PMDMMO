package com.darkxell.client.mechanics.event;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;

public class StairLandingEvent extends DungeonEvent {

    public StairLandingEvent() {
        super(Persistence.floor, DungeonEventSource.CLIENT_PURPUSES);
    }

    @Override
    public String loggerMessage() {
        return null;
    }

}

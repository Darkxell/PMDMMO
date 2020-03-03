package com.darkxell.client.mechanics.event;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.common.event.Event;

public class StairLandingEvent extends Event {

    public StairLandingEvent() {
        super(Persistence.floor, BaseEventSource.CLIENT_PURPUSES);
    }

    @Override
    public String loggerMessage() {
        return null;
    }

}

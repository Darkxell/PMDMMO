package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.util.language.Message;

public class FloorStatusEndedEvent extends Event {
    public final ActiveFloorStatus status;

    public FloorStatusEndedEvent(Floor floor, DungeonEventSource eventSource, ActiveFloorStatus status) {
        super(floor, eventSource);
        this.status = status;
    }

    @Override
    public String loggerMessage() {
        return this.status.status.name() + " finished.";
    }

    @Override
    public ArrayList<Event> processServer() {
        if (this.floor.hasStatus(this.status.status)) {
            this.floor.removeFloorStatus(this.status);
            this.status.onStatusEnd(this.floor, this.resultingEvents);
            Message m = this.status.endMessage();
            if (m != null)
                this.messages.add(m);
        }
        return super.processServer();
    }

}

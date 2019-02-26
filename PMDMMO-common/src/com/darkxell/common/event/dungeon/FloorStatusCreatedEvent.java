package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.util.language.Message;

public class FloorStatusCreatedEvent extends Event {
    public final ActiveFloorStatus status;

    public FloorStatusCreatedEvent(Floor floor, DungeonEventSource eventSource, ActiveFloorStatus status) {
        super(floor, eventSource);
        this.status = status;
    }

    @Override
    public String loggerMessage() {
        return "Created " + this.status.status.name();
    }

    @Override
    public ArrayList<Event> processServer() {
        if (this.floor.hasStatus(this.status.status)) {
            Message m = new Message("status.floor.already").addReplacement("<status>", this.status.status.name());
            this.resultingEvents.add(new MessageEvent(this.floor, this, m));
        } else {
            this.floor.addFloorStatus(this.status);
            Message m = this.status.startMessage();
            if (m != null)
                this.messages.add(m);
            this.status.onStatusStart(floor, this.resultingEvents);
        }
        return super.processServer();
    }

}

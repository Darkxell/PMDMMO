package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.dungeon.FloorStatusEndedEvent;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class ActiveFloorStatus implements DungeonEventSource {

    public final int duration;
    public final Object source;
    public final FloorStatus status;
    /** The number of turns this Status has been in effect. */
    public int tick;

    public ActiveFloorStatus(FloorStatus status, Object source, int duration) {
        this.status = status;
        this.source = source;
        this.duration = duration;
        this.tick = 0;
    }

    public Message endMessage() {
        String id = "status.floor.end." + this.status.id;
        if (!Localization.containsKey(id))
            return null;
        return new Message(id);
    }

    public void finish(Floor floor, ArrayList<DungeonEvent> events) {
        events.add(new FloorStatusEndedEvent(floor, this, this));
    }

    public int getTurns() {
        return this.tick;
    }

    public boolean isOver() {
        if (this.duration == -1)
            return false;
        return this.tick >= this.duration;
    }

    public void onStatusEnd(Floor floor, ArrayList<DungeonEvent> events) {
        this.status.onEnd(floor, this, events);
    }

    public void onStatusStart(Floor floor, ArrayList<DungeonEvent> events) {
        this.status.onStart(floor, this, events);
    }

    public Message startMessage() {
        String id = "status.floor.start." + this.status.id;
        if (!Localization.containsKey(id))
            return null;
        return new Message(id);
    }

    public void tick(Floor floor, ArrayList<DungeonEvent> events) {
        if (!this.isOver())
            this.status.tick(floor, this, events);
        ++this.tick;
        if (this.isOver())
            this.finish(floor, events);
    }

}

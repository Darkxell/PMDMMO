package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.util.language.Message;

public class TrapDestroyedEvent extends DungeonEvent {

    public final Tile tile;

    public TrapDestroyedEvent(Floor floor, Tile tile) {
        super(floor, eventSource);
        this.tile = tile;
    }

    @Override
    public String loggerMessage() {
        return "Trap on tile " + tile + " was destroyed.";
    }

    @Override
    public boolean isValid() {
        return this.tile.trap != null;
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.messages.add(new Message("trap.destroyed").addReplacement("<trap>", this.tile.trap.name()));
        this.tile.trap = null;
        this.tile.trapRevealed = false;
        return super.processServer();
    }

}

package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;

public class TileTypeChangedEvent extends Event {

    public final Tile tile;
    public final TileType tileType;

    public TileTypeChangedEvent(Floor floor, EventSource eventSource, Tile tile, TileType tileType) {
        super(floor, eventSource);
        this.tile = tile;
        this.tileType = tileType;
    }

    @Override
    public String loggerMessage() {
        return this.tile + "'s type changes to " + this.tileType;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.tile.setType(this.tileType);
        return super.processServer();
    }

}

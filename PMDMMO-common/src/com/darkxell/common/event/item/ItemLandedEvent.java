package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.ItemStack;

public class ItemLandedEvent extends DungeonEvent {

    private Tile destination;
    public final ItemStack item;
    private int[][] landingSpots = new int[][] { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, -1 },
            { 1, -1 }, { 1, 1 }, { -2, 0 }, { 0, -2 }, { 2, 0 }, { 0, 2 }, { -1, 2 }, { -2, 1 }, { -2, -1 }, { -1, -2 },
            { 1, -2 }, { 2, -1 }, { 2, 1 }, { 1, 2 } };
    public final Tile tile;

    public ItemLandedEvent(Floor floor, ItemStack item, Tile tile) {
        super(floor, eventSource);
        this.item = item;
        this.tile = tile;
    }

    public Tile destination() {
        return this.destination;
    }

    @Override
    public String loggerMessage() {
        return this.item + " lands on " + this.tile;
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.destination = this.tile;
        int spot = -1;
        while (this.destination.canAccept(this.item) == -1 || this.destination.isWall()) {
            ++spot;
            if (spot >= this.landingSpots.length)
                break;
            this.destination = this.tile.adjacentTile(this.landingSpots[spot][0], this.landingSpots[spot][1]);
        }
        if (this.destination.canAccept(this.item) != -1 && spot < this.landingSpots.length) {
            this.destination.addItem(this.item);
            this.floor.dungeon.communication.itemIDs.register(this.item, null);
        } else
            this.destination = this.tile;
        return super.processServer();
    }

}

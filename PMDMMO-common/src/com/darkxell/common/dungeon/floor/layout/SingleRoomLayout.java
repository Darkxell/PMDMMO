package com.darkxell.common.dungeon.floor.layout;

import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.dungeon.floor.room.SquareRoom;

/** A single room using the hole floor, always a Monster house. */
public class SingleRoomLayout extends Layout {

    @Override
    protected void generateLiquids() {
    }

    @Override
    protected void generatePaths() {
    }

    @Override
    protected void generateRooms() {
        this.createDefaultTiles(56, 32);

        this.floor.rooms = new Room[1];
        this.floor.rooms[0] = new SquareRoom(this.floor, 2, 2, 52, 28, true);
        this.fillRoomsWithGround();
    }

}

package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.common.dungeon.floor.Tile;

public class CustomFloorDungeonTileset extends FloorDungeonTileset {

    public CustomFloorDungeonTileset(int id, String path) {
        super(id, path);
    }

    @Override
    public BufferedImage defaultTile() {
        return this.tileAt(0, 0);
    }

    @Override
    public BufferedImage tile(Tile tile) {
        return this.tileAt(tile.x, tile.y);
    }

}

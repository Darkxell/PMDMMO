package com.darkxell.client.resources.image.tileset.dungeon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;

public abstract class AbstractDungeonTileset extends PMDRegularSpriteset {
    /** The size of a Tile in Dungeons. */
    public static final int TILE_SIZE = 24;

    public AbstractDungeonTileset(String path, int width, int height) {
        super(path, TILE_SIZE, TILE_SIZE, width, height);
    }

    /** @return The tile at the given x, y coordinates. */
    public BufferedImage tileAt(int x, int y) {
        if (x >= 0 && x < this.columns() && y >= 0 && y < this.rows())
            return this.getSprite(x, y);
        return this.getSprite(0, 0);
    }

}

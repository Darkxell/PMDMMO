package com.darkxell.client.mechanics.freezones;

import java.awt.image.BufferedImage;

/**
 * A typical 8*8 tile found in freezones. Freezones are the areas where you can move freely and don't have to fight.
 */
public class FreezoneTile {
    public static final byte TYPE_SOLID = 0;
    public static final byte TYPE_WALKABLE = 1;

    private String tileset;
    private int tilesetX, tilesetY;
    public byte type;

    public FreezoneTile(byte type) {
        this.type = type;
    }

    public BufferedImage getSprite(FreezoneMap map) {
        return map.getTileset(this.tileset).getSprite(this.tilesetX, this.tilesetY);
    }

    public FreezoneTile setTileSprite(String tileset, int tilesetX, int tilesetY) {
        this.tileset = tileset;
        this.tilesetX = tilesetX;
        this.tilesetY = tilesetY;
        return this;
    }

}

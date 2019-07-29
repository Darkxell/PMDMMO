package com.darkxell.client.mechanics.freezones;

import java.awt.image.BufferedImage;

/**
 * A typical 8*8 tile found in freezones. Freezones are the areas where you can move freely and don't have to fight.
 */
public class FreezoneTile {
    public static final byte TYPE_SOLID = 0;
    public static final byte TYPE_WALKABLE = 1;

    public byte type;
    public BufferedImage sprite;

    public FreezoneTile(byte type, BufferedImage sprite) {
        this.type = type;
        this.sprite = sprite;
    }

}

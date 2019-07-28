package com.darkxell.client.resources.image.dungeon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;

public class DungeonMapTileset extends PMDRegularSpriteset {

    public DungeonMapTileset() {
        super("/tilesets/dungeon/map.png", 4, 4, 5, 5);
    }

    public BufferedImage ally() {
        return this.getSprite(2, 0);
    }

    public BufferedImage enemy() {
        return this.getSprite(0, 1);
    }

    public BufferedImage ground() {
        return this.getSprite(0, 2);
    }

    public BufferedImage item() {
        return this.getSprite(4, 0);
    }

    public BufferedImage other() {
        return this.getSprite(3, 0);
    }

    public BufferedImage outlaw() {
        return this.getSprite(1, 1);
    }

    public BufferedImage player() {
        return this.getSprite(0, 0);
    }

    public BufferedImage stairs() {
        return this.getSprite(2, 2);
    }

    public BufferedImage trap() {
        return this.getSprite(3, 2);
    }

    public BufferedImage warpzone() {
        return this.getSprite(4, 2);
    }

    public BufferedImage wonder() {
        return this.getSprite(1, 2);
    }

}

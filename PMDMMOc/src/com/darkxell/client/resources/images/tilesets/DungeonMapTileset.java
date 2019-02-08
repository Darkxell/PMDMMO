package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.RegularSpriteSet;

public class DungeonMapTileset extends RegularSpriteSet {

    public DungeonMapTileset() {
        super("/tilesets/dungeon/map.png", 4, 20, 20);
    }

    public BufferedImage ally() {
        return this.getImg(2, 0);
    }

    public BufferedImage enemy() {
        return this.getImg(0, 1);
    }

    public BufferedImage ground() {
        return this.getImg(0, 2);
    }

    public BufferedImage item() {
        return this.getImg(4, 0);
    }

    public BufferedImage other() {
        return this.getImg(3, 0);
    }

    public BufferedImage outlaw() {
        return this.getImg(1, 1);
    }

    public BufferedImage player() {
        return this.getImg(0, 0);
    }

    public BufferedImage stairs() {
        return this.getImg(2, 2);
    }

    public BufferedImage trap() {
        return this.getImg(3, 2);
    }

    public BufferedImage warpzone() {
        return this.getImg(4, 2);
    }

    public BufferedImage wonder() {
        return this.getImg(1, 2);
    }

}

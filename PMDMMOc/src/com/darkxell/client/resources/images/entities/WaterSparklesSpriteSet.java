package com.darkxell.client.resources.images.entities;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;

public class WaterSparklesSpriteSet extends SpriteSet {

    public WaterSparklesSpriteSet() {
        super("/freezones/entities/watersparkles.png", 192, 105);
        this.createSpriteRow("side", 0, 0, 19, 41, 6);
        this.createSpriteRow("bot", 0, 42, 19, 29, 6);
        this.createSpriteRow("top", 0, 72, 19, 23, 6);
        this.createSpriteRow("long", 0, 96, 32, 8, 6);
    }

    public BufferedImage bot(byte state) {
        return this.getImg("bot" + state);
    }

    public BufferedImage lon(byte state) {
        return this.getImg("long" + state);
    }

    public BufferedImage side(byte state) {
        return this.getImg("side" + state);
    }

    public BufferedImage top(byte state) {
        return this.getImg("top" + state);
    }

}

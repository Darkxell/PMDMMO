package com.darkxell.client.resources.image.freezoneentity;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class WaterSparklesSpriteSet extends PMDSpriteset {

    public WaterSparklesSpriteSet() {
        super("/freezones/entities/watersparkles.png");
        this.createSpriteRow("side", 0, 0, 19, 41, 6);
        this.createSpriteRow("bot", 0, 42, 19, 29, 6);
        this.createSpriteRow("top", 0, 72, 19, 23, 6);
        this.createSpriteRow("long", 0, 96, 32, 8, 6);
    }

    public BufferedImage bot(byte state) {
        return this.getSprite("bot" + state);
    }

    public BufferedImage lon(byte state) {
        return this.getSprite("long" + state);
    }

    public BufferedImage side(byte state) {
        return this.getSprite("side" + state);
    }

    public BufferedImage top(byte state) {
        return this.getSprite("top" + state);
    }

}

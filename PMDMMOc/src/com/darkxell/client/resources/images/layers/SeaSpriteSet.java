package com.darkxell.client.resources.images.layers;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;

public class SeaSpriteSet extends SpriteSet {

    public SeaSpriteSet() {
        super("/graphicallayers/water.png", 720, 376);
        this.createSprite("clouds", 0, 0, 720, 208);
        this.createSpriteRow("horizon", 0, 208, 48, 128, this.horizonCount());
        this.createSpriteRow("waves", 240, 208, 48, 168, this.wavesCount());
    }

    public BufferedImage clouds() {
        return this.getImg("clouds");
    }

    public BufferedImage horizon(int tick) {
        return this.getImg("horizon" + tick);
    }

    public int horizonCount() {
        return 5;
    }

    public BufferedImage waves(int tick) {
        return this.getImg("waves" + tick);
    }

    public int wavesCount() {
        return 10;
    }

}

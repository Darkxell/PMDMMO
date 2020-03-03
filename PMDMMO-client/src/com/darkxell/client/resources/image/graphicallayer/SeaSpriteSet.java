package com.darkxell.client.resources.image.graphicallayer;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class SeaSpriteSet extends PMDSpriteset {

    public SeaSpriteSet() {
        super("/graphicallayers/water.png");
        this.createSprite("clouds", 0, 0, 720, 208);
        this.createSpriteRow("horizon", 0, 208, 48, 128, this.horizonCount());
        this.createSpriteRow("waves", 240, 208, 48, 168, this.wavesCount());
    }

    public BufferedImage clouds() {
        return this.getSprite("clouds");
    }

    public BufferedImage horizon(int tick) {
        if (tick >= this.horizonCount() || tick < 0)
            return this.horizon(0);
        return this.getSprite("horizon" + tick);
    }

    public int horizonCount() {
        return 5;
    }

    public BufferedImage waves(int tick) {
        if (tick >= this.wavesCount() || tick < 0)
            return this.waves(0);
        return this.getSprite("waves" + tick);
    }

    public int wavesCount() {
        return 10;
    }

}

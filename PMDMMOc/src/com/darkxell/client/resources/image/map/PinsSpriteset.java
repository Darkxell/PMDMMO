package com.darkxell.client.resources.image.map;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;

public class PinsSpriteset extends PMDRegularSpriteset {

    public PinsSpriteset() {
        super("/hud/map/pins.png", 12, 12, 4, 1);
    }

    public BufferedImage blue() {
        return this.getSprite(2);
    }

    public BufferedImage green() {
        return this.getSprite(3);
    }

    public BufferedImage red() {
        return this.getSprite(0);
    }

    public BufferedImage yellow() {
        return this.getSprite(1);
    }

}

package com.darkxell.client.resources.image.graphicallayer;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class LSDSpriteSet extends PMDSpriteset {

    public LSDSpriteSet() {
        super("/graphicallayers/lsd.png");
        this.createSpriteRow("top", 0, 0, 42, 160, 8);
        this.createSpriteRow("bottom", 0, 160, 42, 160, 8);
    }

    public BufferedImage bottom(int color) {
        return this.getSprite("bottom" + color);
    }

    public BufferedImage top(int color) {
        return this.getSprite("top" + color);
    }

}

package com.darkxell.client.resources.images.layers;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;

public class LSDSpriteSet extends SpriteSet {

    public LSDSpriteSet() {
        super("/graphicallayers/lsd.png", 336, 320);
        this.createSpriteRow("top", 0, 0, 42, 160, 8);
        this.createSpriteRow("bottom", 0, 160, 42, 160, 8);
    }

    public BufferedImage bottom(int color) {
        return this.getImg("bottom" + color);
    }

    public BufferedImage top(int color) {
        return this.getImg("top" + color);
    }

}

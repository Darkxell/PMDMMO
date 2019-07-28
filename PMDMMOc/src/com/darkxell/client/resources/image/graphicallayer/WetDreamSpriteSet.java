package com.darkxell.client.resources.image.graphicallayer;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class WetDreamSpriteSet extends PMDSpriteset {

    public WetDreamSpriteSet() {
        super("/graphicallayers/dream.png");
        createSprite("bigleft", 0, 0, 147, 192);
        createSprite("smallleft", 147, 0, 147, 192);
        createSprite("smallright", 294, 0, 147, 192);
        createSprite("bigright", 441, 0, 147, 192);
    }

    public BufferedImage getSmallLeft() {
        return this.getSprite("smallleft");
    }

    public BufferedImage getSmallRight() {
        return this.getSprite("smallright");
    }

    public BufferedImage getBigLeft() {
        return this.getSprite("bigleft");
    }

    public BufferedImage getBigRight() {
        return this.getSprite("bigright");
    }

}

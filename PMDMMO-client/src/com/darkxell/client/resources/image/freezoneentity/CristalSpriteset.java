package com.darkxell.client.resources.image.freezoneentity;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class CristalSpriteset extends PMDSpriteset {

    public CristalSpriteset() {
        super("/freezones/entities/cristal.png");
        this.createSprite("red", 1, 54, 56, 81);
        this.createSprite("yellow", 58, 54, 56, 81);
        this.createSprite("lightray", 115, 1, 48, 134);
    }

    public BufferedImage lightray() {
        return this.getSprite("lightray");
    }

    public BufferedImage red() {
        return this.getSprite("red");
    }

    public BufferedImage yellow() {
        return this.getSprite("yellow");
    }

}

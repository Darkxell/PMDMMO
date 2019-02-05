package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;

public class ShadowSpriteSet extends SpriteSet {

    public ShadowSpriteSet() {
        super("/pokemons/shadows.png", 20, 64);
        this.createSprite("small0", 0, 0, 14, 6);
        this.createSprite("small1", 0, 6, 14, 6);
        this.createSprite("small2", 0, 12, 14, 6);
        this.createSprite("small3", 0, 18, 14, 6);
        this.createSprite("big0", 0, 24, 20, 10);
        this.createSprite("big1", 0, 34, 20, 10);
        this.createSprite("big2", 0, 44, 20, 10);
        this.createSprite("big3", 0, 54, 20, 10);
    }

    public BufferedImage getBig(byte shadowColor) {
        return this.getImg("big" + shadowColor);
    }

    public BufferedImage getSmall(byte shadowColor) {
        return this.getImg("small" + shadowColor);
    }

}

package com.darkxell.client.resources.image.spritefactory;

import java.awt.image.BufferedImage;

/**
 * Accessor to sprite images.
 */
public class PMDSprite {

    /**
     * The path to the image.
     */
    public final String path;

    public PMDSprite(String path) {
        this.path = path;
        PMDSpriteFactory.instance().load(this);
    }

    /**
     * 
     * @return The image this Sprite points to.
     */
    public BufferedImage image() {
        return PMDSpriteFactory.instance().get(this.path);
    }

    /**
     * 
     * @return Is the image loaded by the Sprite Factory?
     */
    public boolean isLoaded() {
        return PMDSpriteFactory.instance().isLoaded(this.path);
    }

}

package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

/** Holds an Image. Allows proper loading using the SpriteFactory. */
public class Sprite {

    private BufferedImage image;
    protected boolean loaded = false;
    public final String path;

    public Sprite(BufferedImage image) {
        this.path = "";
        this.image = image;
    }

    public Sprite(String path) {
        this(path, -1, -1);
    }

    public Sprite(String path, int width, int height) {
        this(path, width, height, true);
    }

    Sprite(String path, int width, int height, boolean doLoad) {
        this.path = path;
        if (doLoad) this.image = SpriteFactory.instance().load(this, this.path, width, height);
    }

    public void dispose() {
        SpriteFactory.instance().dispose(this.path);
    }

    /** @return The Image held in this Sprite. */
    public BufferedImage image() {
        return this.image;
    }

    /** @return <code>true</code> if this Sprite is loaded. */
    public boolean isLoaded() {
        return this.loaded;
    }

    /** This method is called when the SpriteFactory loads this Sprite. If overriding this method, the parent should always be called to set the base image.<br>
     * This method is called by the SpriteFactory's Thread and should be thread-safe.
     * 
     * @param img - The Image after loading. */
    protected void loaded(BufferedImage img) {
        this.loaded = true;
        this.image = img;
    }

}

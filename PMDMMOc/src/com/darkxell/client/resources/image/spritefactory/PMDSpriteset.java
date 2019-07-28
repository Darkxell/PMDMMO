package com.darkxell.client.resources.image.spritefactory;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;

public abstract class PMDSpriteset extends PMDSprite {

    /**
     * Holds all sprites of this Spriteset.
     */
    private final HashMap<String, BufferedImage> images = new HashMap<>();
    /**
     * Defines each part of this Spriteset.
     */
    private final HashMap<String, Rectangle> parts = new HashMap<>();
    private boolean wasLoaded = false;

    public PMDSpriteset(String path) {
        super(path);
        this.wasLoaded = this.isLoaded();

        this.createSprites();
    }

    private void checkLoaded() {
        if (this.wasLoaded)
            return;
        if (this.isLoaded()) {
            this.wasLoaded = true;
            this.reloadParts();
        }
    }

    protected void createSprite(String id, Rectangle location) {
        this.parts.put(id, location);
        this.images.put(id, Res.createimage(this.image(), location.x, location.y, location.width, location.height));
    }

    protected abstract void createSprites();

    public BufferedImage getSprite(String id) {
        this.checkLoaded();
        if (this.images.containsKey(id))
            return this.images.get(id);
        Logger.w("Attempted to get a sprite with unknown ID from a spriteset.");
        return PMDSpriteFactory.instance().getDefault();
    }

    private void reloadParts() {
        for (String path : this.parts.keySet()) {
            Rectangle r = this.parts.get(path);
            this.images.put(path, Res.createimage(this.image(), r.x, r.y, r.width, r.height));
        }
    }

}

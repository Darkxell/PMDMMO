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
    }

    private void checkLoaded() {
        if (this.wasLoaded)
            return;
        if (this.isLoaded()) {
            this.wasLoaded = true;
            this.onLoad();
            this.reloadParts();
        }
    }

    protected void createSprite(String id, int x, int y, int width, int height) {
        this.createSprite(id, new Rectangle(x, y, width, height));
    }

    protected void createSprite(String id, Rectangle location) {
        this.parts.put(id, location);
        this.images.put(id, Res.createimage(this.image(), location.x, location.y, location.width, location.height));
    }

    /**
     * Creates several sprites in a row.
     * 
     * @param baseID      - The baseID of the sprite. The index of the sprite in the row will be added to it as a
     *                    suffix.
     * @param startX      startY - The starting topleft position of the row.
     * @param spriteWidth spriteHeight - The size of each sprite.
     * @param count       - The number of sprites to create.
     */
    protected void createSpriteRow(String baseID, int startX, int startY, int spriteWidth, int spriteHeight,
            int count) {
        for (int i = 0; i < count; ++i)
            this.createSprite(baseID + i, startX + i * spriteWidth, startY, spriteWidth, spriteHeight);
    }

    public BufferedImage getSprite(String id) {
        this.checkLoaded();
        if (this.images.containsKey(id))
            return this.images.get(id);
        Logger.w("Attempted to get a sprite with unknown ID from a spriteset: " + id);
        return PMDSpriteFactory.instance().getDefault();
    }

    protected void onLoad() {
    }

    private void reloadParts() {
        for (String path : this.parts.keySet()) {
            Rectangle r = this.parts.get(path);
            this.images.put(path, Res.createimage(this.image(), r.x, r.y, r.width, r.height));
        }
    }

}

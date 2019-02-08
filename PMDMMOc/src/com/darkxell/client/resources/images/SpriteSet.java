package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/** Represents a set of Sprites derived from a single Sprite. */
public class SpriteSet extends Sprite {

    private HashMap<String, SubSprite> sprites = new HashMap<>();

    public SpriteSet(String path) {
        super(path);
    }

    public SpriteSet(String path, int width, int height) {
        super(path, width, height);
    }

    public SpriteSet(String path, int width, int height, boolean doLoad) {
        super(path, width, height, doLoad);
    }

    /**
     * Registers a Sprite in this Spriteset.
     *
     * @param  id - The ID of the Sprite.
     * @param  x  <b>y width height</b> - The part of this Spriteset's base Image.
     * @return    The created Sprite.
     */
    public Sprite createSprite(String id, int x, int y, int width, int height) {
        this.sprites.put(id, new SubSprite(this.path, x, y, width, height));
        return this.sprites.get(id);
    }

    /**
     * Registers several Sprites in this Spriteset, loaded from a row from the source.
     *
     * @param  id     - The prefix ID of the Sprite. Each final Sprite will have as an id: [prefix][index].
     * @param  xStart <b>yStart</b> - Where the row starts.
     * @param  width  <b>height</b> - The dimensions of each Sprite.
     * @param  count  - The number of Sprites to create.
     * @return        The created Sprites.
     */
    public Sprite[] createSpriteRow(String id, int xStart, int yStart, int width, int height, int count) {
        Sprite[] sprites = new Sprite[count];
        for (int i = 0; i < count; ++i)
            this.createSprite(id + i, xStart + i * width, yStart, width, height);
        return sprites;
    }

    /**
     * @param  id - The ID of one of this Spriteset's Sprites.
     * @return    The Sprite with the input ID.
     */
    public SubSprite get(String id) {
        this.checkIfLoaded();
        return this.sprites.get(id);
    }

    /**
     * @param  id - The ID of one of this Spriteset's Sprites.
     * @return    The Image of the Sprite with the input ID.
     */
    public BufferedImage getImg(String id) {
        SubSprite s = this.get(id);
        if (s == null)
            return null;
        return s.image();
    }

    /** @return The number of Sprites in this SpriteSet. */
    public int spriteCount() {
        return this.sprites.size();
    }

}

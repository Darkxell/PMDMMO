package com.darkxell.client.resources.image.spritefactory;

import java.awt.image.BufferedImage;

/**
 * A Spriteset which has all sprites with the same width and height, and all consecutive. May span several rows and/or
 * columns.
 */
public class PMDRegularSpriteset extends PMDSpriteset {

    private int columns = -1, rows = -1;
    public final int spriteWidth, spriteHeight;

    public PMDRegularSpriteset(String path, int size) {
        this(path, size, size);
    }

    public PMDRegularSpriteset(String path, int width, int height) {
        this(path, width, height, -1, -1);
    }

    public PMDRegularSpriteset(String path, int width, int height, int expectedColumns, int expectedRows) {
        super(path);
        this.spriteWidth = width;
        this.spriteHeight = height;
        this.columns = expectedColumns;
        this.rows = expectedRows;
        this.createSprite(this.spriteId(0, 0), 0, 0, this.spriteWidth, this.spriteHeight);

        if (this.isLoaded() || (this.columns != -1 && this.rows != -1))
            this.onLoad();
    }

    public int columns() {
        return this.columns;
    }

    /**
     * @return The default sprite for this Spriteset.
     */
    public BufferedImage getDefault() {
        return this.getSprite(this.spriteId(0, 0));
    }

    /**
     * @return The sprite at the input position, where pos = x + y * columns.
     */
    public BufferedImage getSprite(int pos) {
        if (this.columns == -1 || this.rows == -1)
            return this.getDefault();
        return this.getSprite(pos % this.columns, pos / this.columns);
    }

    /**
     * @return The sprite at the input x,y position.
     */
    public BufferedImage getSprite(int x, int y) {
        if (x < 0 || y < 0 || x >= this.columns || y >= this.rows)
            return this.getDefault();
        return this.getSprite(this.spriteId(x, y));
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (this.columns == -1)
            this.columns = this.image().getWidth() / this.spriteWidth;
        if (this.rows == -1)
            this.rows = this.image().getHeight() / this.spriteHeight;

        if (this.columns == 0)
            this.columns = 1;
        if (this.rows == 0)
            this.rows = 1;

        for (int x = 0; x < this.columns; ++x)
            for (int y = 0; y < this.rows; ++y)
                this.createSprite(this.spriteId(x, y), this.spriteWidth * x, this.spriteHeight * y, this.spriteWidth,
                        this.spriteHeight);
    }

    public int rows() {
        return this.rows;
    }

    /**
     * @return The ID for the sprite at the input x,y position.
     */
    private String spriteId(int x, int y) {
        return x + "," + y;
    }

}

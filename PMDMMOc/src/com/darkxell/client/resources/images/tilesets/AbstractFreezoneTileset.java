package com.darkxell.client.resources.images.tilesets;

import com.darkxell.client.resources.images.RegularSpriteSet;

/** Represents a tileset. A tileset contains all of the images used in a certain dungeon. */
public class AbstractFreezoneTileset extends RegularSpriteSet {
    /** The size of a Tile in Freezones. */
    public static final int TILE_SIZE = 8;

    /** Returns a tileset made from the parsed tileset image file path. */
    private static AbstractFreezoneTileset getTileset(String path, int width, int height) {
        return new AbstractFreezoneTileset(path, width, height);
    }

    /**
     * Retrieves a tileset from the {@code /tilesets/*.png} folder.
     *
     * The name should usually correspond to the terrain file, though this is not guaranteed.
     */
    public static AbstractFreezoneTileset getTileSet(String code, int width, int height) {
        return getTileset("/tilesets/" + code + ".png", width, height);
    }

    public AbstractFreezoneTileset(String path, int width, int height) {
        super(path, TILE_SIZE, width, height);
    }
}

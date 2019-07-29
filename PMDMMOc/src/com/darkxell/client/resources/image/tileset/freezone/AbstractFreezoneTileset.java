package com.darkxell.client.resources.image.tileset.freezone;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;

/** Represents a tileset. A tileset contains all of the images used in a certain dungeon. */
public class AbstractFreezoneTileset extends PMDRegularSpriteset {

    /** The size of a Tile in Freezones. */
    public static final int TILE_SIZE = 8;

    /**
     * Retrieves a tileset from the {@code /tilesets/*.png} folder.
     *
     * The name should usually correspond to the terrain file, though this is not guaranteed.
     */
    public static AbstractFreezoneTileset getTileSet(String code, int width, int height) {
        return new AbstractFreezoneTileset("/tilesets/" + code + ".png", width, height);
    }

    public AbstractFreezoneTileset(String path, int width, int height) {
        super(path, TILE_SIZE, TILE_SIZE, width, height);
    }
}

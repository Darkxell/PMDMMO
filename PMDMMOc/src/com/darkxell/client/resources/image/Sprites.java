package com.darkxell.client.resources.image;

import com.darkxell.client.resources.image.dungeon.ItemsSpriteset;
import com.darkxell.common.util.Logger;

/**
 * This class holds all Sprites that are used regularly throughout the game.
 */
public final class Sprites {

    public static class DungeonSprites {
        public static final ItemsSpriteset items = new ItemsSpriteset();
    }

    public static void load() {
        Logger.i("Loading common sprites...");
    }

    private Sprites() {
    }

}

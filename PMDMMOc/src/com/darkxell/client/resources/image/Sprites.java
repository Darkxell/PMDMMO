package com.darkxell.client.resources.image;

import com.darkxell.client.resources.image.dungeon.CommonDungeonTileset;
import com.darkxell.client.resources.image.dungeon.DungeonHudSpriteset;
import com.darkxell.client.resources.image.dungeon.DungeonMapTileset;
import com.darkxell.client.resources.image.dungeon.ItemsSpriteset;
import com.darkxell.client.resources.image.dungeon.ShadowSpriteSet;
import com.darkxell.client.resources.image.graphicallayer.LSDSpriteSet;
import com.darkxell.client.resources.image.graphicallayer.SeaSpriteSet;
import com.darkxell.client.resources.image.graphicallayer.WetDreamSpriteSet;
import com.darkxell.common.util.Logger;

/**
 * This class holds all Sprites that are used regularly throughout the game.
 */
public final class Sprites {

    public static class DungeonSprites {
        public static final CommonDungeonTileset dungeonCommon = new CommonDungeonTileset();
        public static final DungeonHudSpriteset dungeonHud = new DungeonHudSpriteset();
        public static final DungeonMapTileset dungeonMap = new DungeonMapTileset();
        public static final ItemsSpriteset items = new ItemsSpriteset();
        public static final ShadowSpriteSet shadows = new ShadowSpriteSet();
    }
    
    public static class GraphicalLayerSprites {
        public static final LSDSpriteSet LSD = new LSDSpriteSet();
        public static final SeaSpriteSet Sea = new SeaSpriteSet();
        public static final WetDreamSpriteSet Dream = new WetDreamSpriteSet();
    }

    public static void load() {
        Logger.i("Loading common sprites...");
    }

    private Sprites() {
    }

}

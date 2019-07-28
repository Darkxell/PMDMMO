package com.darkxell.client.resources.image;

import com.darkxell.client.resources.image.dungeon.CommonDungeonTileset;
import com.darkxell.client.resources.image.dungeon.DungeonHudSpriteset;
import com.darkxell.client.resources.image.dungeon.DungeonMapTileset;
import com.darkxell.client.resources.image.dungeon.ItemsSpriteset;
import com.darkxell.client.resources.image.dungeon.ShadowSpriteSet;
import com.darkxell.client.resources.image.frame.BoxSpriteset;
import com.darkxell.client.resources.image.freezoneentity.CristalSpriteset;
import com.darkxell.client.resources.image.freezoneentity.WaterSparklesSpriteSet;
import com.darkxell.client.resources.image.graphicallayer.LSDSpriteSet;
import com.darkxell.client.resources.image.graphicallayer.SeaSpriteSet;
import com.darkxell.client.resources.image.graphicallayer.WetDreamSpriteSet;
import com.darkxell.client.resources.image.hud.FontSpriteSet;
import com.darkxell.client.resources.image.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.image.map.PinsSpriteset;
import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.ui.Frame.FrameIconSprite;

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

    public static class FrameSprites {
        public static final Sprite BG1 = new Sprite("/hud/framebackgrounds/1.jpg");
        public static final Sprite BG2 = new Sprite("/hud/framebackgrounds/2.jpg");
        public static final Sprite BG3 = new Sprite("/hud/framebackgrounds/3.png");
        public static final Sprite BG4 = new Sprite("/hud/framebackgrounds/4.png");
        public static final Sprite BG5 = new Sprite("/hud/framebackgrounds/5.jpg");
        public static final Sprite BG6 = new Sprite("/hud/framebackgrounds/6.png");
        public static final Sprite BG7 = new Sprite("/hud/framebackgrounds/7.jpg");
        public static final Sprite ICON = new FrameIconSprite("/hud/framebackgrounds/icon.png");
        
        public static final BoxSpriteset box = new BoxSpriteset();
    }

    public static class FreezoneEntitySprites {
        public static final CristalSpriteset cristals = new CristalSpriteset();
        public static final PMDRegularSpriteset flag = new PMDRegularSpriteset("/freezones/entities/flag.png", 32, 8,
                192, 8);
        public static final PMDRegularSpriteset RedFlower = new PMDRegularSpriteset("/freezones/entities/redflower.png",
                32, 32, 192, 32);
        public static final WaterSparklesSpriteSet waterSparkles = new WaterSparklesSpriteSet();
        public static final PMDRegularSpriteset YellowFlower = new PMDRegularSpriteset(
                "/freezones/entities/yellowflower.png", 32, 32, 192, 32);
    }

    public static class GraphicalLayerSprites {
        public static final WetDreamSpriteSet Dream = new WetDreamSpriteSet();
        public static final LSDSpriteSet LSD = new LSDSpriteSet();
        public static final SeaSpriteSet Sea = new SeaSpriteSet();
    }

    public static class HudSprites {
        public static final Sprite button = new Sprite("/hud/button.png");
        public static final Sprite createaccountframe = new Sprite("/hud/create.png");

        public static final FontSpriteSet font = new FontSpriteSet();
        public static final Sprite gametitle = new Sprite("/hud/title.png");
        public static final Sprite loginframe = new Sprite("/hud/login.png");
        public static final MenuStateHudSpriteset menuHud = new MenuStateHudSpriteset();
        public static final Sprite portrait = new Sprite("/hud/portrait.png");
        public static final Sprite proceedaccountframe = new Sprite("/hud/create2.png");
        public static final Sprite textwindow = new Sprite("/hud/textwindow.png");
        public static final Sprite textwindow_transparent = new Sprite("/hud/textwindow_transparent.png");
    }

    public static class MapSprites {
        public static final Sprite globalMap = new Sprite("/hud/map/globalmap.png");
        public static final Sprite localMap = new Sprite("/hud/map/localmap.png");
        public static final PinsSpriteset pins = new PinsSpriteset();
    }

    public static void load() {
    }

    private Sprites() {
    }

}

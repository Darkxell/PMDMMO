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
import com.darkxell.client.resources.image.hud.ChatFooterSpriteset;
import com.darkxell.client.resources.image.hud.ChatIconsSpriteset;
import com.darkxell.client.resources.image.hud.FontSpriteSet;
import com.darkxell.client.resources.image.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.image.map.PinsSpriteset;
import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.client.resources.image.spritefactory.PMDSprite;

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
        public static final PMDSprite BG1 = new PMDSprite("/hud/framebackgrounds/1.jpg");
        public static final PMDSprite BG2 = new PMDSprite("/hud/framebackgrounds/2.jpg");
        public static final PMDSprite BG3 = new PMDSprite("/hud/framebackgrounds/3.png");
        public static final PMDSprite BG4 = new PMDSprite("/hud/framebackgrounds/4.png");
        public static final PMDSprite BG5 = new PMDSprite("/hud/framebackgrounds/5.jpg");
        public static final PMDSprite BG6 = new PMDSprite("/hud/framebackgrounds/6.png");
        public static final PMDSprite BG7 = new PMDSprite("/hud/framebackgrounds/7.jpg");
        public static final BoxSpriteset box = new BoxSpriteset();
        public static final PMDSprite ICON = new PMDSprite("/hud/framebackgrounds/icon.png");
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
        public static final PMDSprite billboard = new PMDSprite("/hud/billboard_list.png");
        public static final PMDSprite billboardDetails = new PMDSprite("/hud/billboard_details.png");
        public static final PMDSprite button = new PMDSprite("/hud/button.png");
        public static final ChatFooterSpriteset chatFooter = new ChatFooterSpriteset();
        public static final ChatIconsSpriteset chatIcons = new ChatIconsSpriteset();
        public static final PMDSprite createaccountframe = new PMDSprite("/hud/create.png");
        public static final FontSpriteSet font = new FontSpriteSet();
        public static final PMDSprite gametitle = new PMDSprite("/hud/title.png");
        public static final PMDSprite loginframe = new PMDSprite("/hud/login.png");
        public static final MenuStateHudSpriteset menuHud = new MenuStateHudSpriteset();
        public static final PMDSprite portrait = new PMDSprite("/hud/portrait.png");
        public static final PMDSprite proceedaccountframe = new PMDSprite("/hud/create2.png");
        public static final PMDSprite textwindow = new PMDSprite("/hud/textwindow.png");
        public static final PMDSprite textwindow_transparent = new PMDSprite("/hud/textwindow_transparent.png");
    }

    public static class MapSprites {
        public static final PMDSprite globalMap = new PMDSprite("/hud/map/globalmap.png");
        public static final PMDSprite localMap = new PMDSprite("/hud/map/localmap.png");
        public static final PinsSpriteset pins = new PinsSpriteset();
    }

    public static void load() {
    }

    private Sprites() {
    }

}

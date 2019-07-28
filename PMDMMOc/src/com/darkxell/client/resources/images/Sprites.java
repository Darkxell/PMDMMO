package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.entities.WaterSparklesSpriteSet;
import com.darkxell.client.resources.images.hud.FontSpriteSet;
import com.darkxell.client.resources.images.hud.MenuStateHudSpriteset;
import com.darkxell.client.ui.Frame.FrameIconSprite;

/** Class that holds Sprites & SpriteSets used in the whole project. */
public final class Sprites {

    public static class Res_Frame {
        public static final Sprite BG1 = new Sprite("/hud/framebackgrounds/1.jpg");
        public static final Sprite BG2 = new Sprite("/hud/framebackgrounds/2.jpg");
        public static final Sprite BG3 = new Sprite("/hud/framebackgrounds/3.png");
        public static final Sprite BG4 = new Sprite("/hud/framebackgrounds/4.png");
        public static final Sprite BG5 = new Sprite("/hud/framebackgrounds/5.jpg");
        public static final Sprite BG6 = new Sprite("/hud/framebackgrounds/6.png");
        public static final Sprite BG7 = new Sprite("/hud/framebackgrounds/7.jpg");
        public static final Sprite box_E, box_N, box_NE, box_NW, box_S, box_SE, box_SW, box_W;
        // why is this here?
        public static final Sprite ICON = new FrameIconSprite("/hud/framebackgrounds/icon.png");

        /** Target file path of the background images. */
        public static final String BACKGROUNDS_DIRECTORY = "hud/framebackgrounds";

        /** Background images ooh pretty. */
        public static final Sprite[] BACKGROUNDS;

        public static final int DEFAULT_BACKGROUND_INDEX = 0;

        /** Which files in the frame background image folder to ignore. */
        public static final String[] EXCLUDE_FILES = new String[] { "icon.png" };

        static {
            String[] backgroundPaths = Res.getResourceFiles(BACKGROUNDS_DIRECTORY);

            if (backgroundPaths.length > 0) {
                BACKGROUNDS = new Sprite[backgroundPaths.length];
                for (int i = 0; i < BACKGROUNDS.length; ++i)
                    BACKGROUNDS[i] = new Sprite(backgroundPaths[i]);
            } else
                BACKGROUNDS = new Sprite[] { SpriteFactory.getDefaultSprite(20, 20) };

            Sprite source = new Sprite("/hud/boxcorners.png");

            box_E = SpriteFactory.instance().subSprite(source, 8, 3, 7, 1);
            box_N = SpriteFactory.instance().subSprite(source, 7, 0, 1, 3);
            box_NE = SpriteFactory.instance().subSprite(source, 8, 0, 7, 3);

            box_NW = SpriteFactory.instance().subSprite(source, 0, 0, 7, 3);
            box_S = SpriteFactory.instance().subSprite(source, 7, 4, 1, 3);
            box_SE = SpriteFactory.instance().subSprite(source, 8, 4, 7, 3);
            box_SW = SpriteFactory.instance().subSprite(source, 0, 4, 7, 3);
            box_W = SpriteFactory.instance().subSprite(source, 0, 3, 7, 1);
        }

        public static BufferedImage getBackground(int id) {
            id--; // backwards compatibility; was 1-indexed
            if (id >= 0 && id < BACKGROUNDS.length) {
                return BACKGROUNDS[id].image();
            }
            return BACKGROUNDS[DEFAULT_BACKGROUND_INDEX].image();
        }

        static void load() {
        }
    }

    public static class Res_FreezoneEntities {
        public static final Sprite cristal_red, cristal_yellow, cristal_lightray;
        public static final RegularSpriteSet flag = new RegularSpriteSet("/freezones/entities/flag.png", 32, 8, 192, 8);
        public static final RegularSpriteSet RedFlower = new RegularSpriteSet("/freezones/entities/redflower.png", 32,
                32, 192, 32);

        public static final WaterSparklesSpriteSet waterSparkles = new WaterSparklesSpriteSet();

        public static final RegularSpriteSet YellowFlower = new RegularSpriteSet("/freezones/entities/yellowflower.png",
                32, 32, 192, 32);

        static {
            Sprite cristal = new Sprite("/freezones/entities/cristal.png");
            cristal_red = SpriteFactory.instance().subSprite(cristal, 1, 54, 56, 81);
            cristal_yellow = SpriteFactory.instance().subSprite(cristal, 58, 54, 56, 81);
            cristal_lightray = SpriteFactory.instance().subSprite(cristal, 115, 1, 48, 134);
        }

        static void load() {
        }
    }

    public static class Res_Hud {
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

        static void load() {
        }
    }

    private Sprites() {
    }

}

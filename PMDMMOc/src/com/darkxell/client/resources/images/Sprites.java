package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
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

    private Sprites() {
    }

}

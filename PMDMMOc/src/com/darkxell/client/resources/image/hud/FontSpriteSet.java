package com.darkxell.client.resources.image.hud;

import java.awt.image.BufferedImage;

import com.darkxell.client.renderers.TextRenderer.PMDChar;
import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class FontSpriteSet extends PMDSpriteset {
    public static final int CHAR_HEIGHT = 11;
    public static final int COLUMNS = 20, ROWS = 20;
    public static final int GRID_WIDTH = CHAR_HEIGHT, GRID_HEIGHT = CHAR_HEIGHT;

    public FontSpriteSet() {
        super("/hud/font.png");

        for (PMDChar c : PMDChar.values())
            if (c.isChar())
                this.createSprite(c.value, c.xPos * GRID_WIDTH, c.yPos * GRID_HEIGHT, c.width, CHAR_HEIGHT);
    }

    public BufferedImage getSprite(PMDChar c) {
        return this.getSprite(c.value);
    }

}

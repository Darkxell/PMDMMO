package com.darkxell.client.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.Sprites.FrameSprites;
import com.darkxell.common.util.Direction;

/**
 * Class that contains various utility methods for the statemanager to display the components on the frame.
 */
public abstract class MainUiUtility {

    private static final int OUTLINE_WIDTH = 16, OUTLINE_HEIGHT = 8;

    public static void drawBackground(Graphics2D g, int fwidth, int fheight, byte backgroundID) {
        BufferedImage img;
        switch (backgroundID) {
        case 2:
            img = FrameSprites.BG2.image();
            break;
        case 3:
            img = FrameSprites.BG3.image();
            break;
        case 4:
            img = FrameSprites.BG4.image();
            break;
        case 5:
            img = FrameSprites.BG5.image();
            break;
        case 6:
            img = FrameSprites.BG6.image();
            break;
        case 7:
            img = FrameSprites.BG7.image();
            break;
        default:
            img = FrameSprites.BG1.image();
            break;
        }
        if ((float) (fwidth) / (float) (fheight) < (float) (img.getWidth()) / (float) (img.getHeight())) {
            int drawwidth = fheight * img.getWidth() / img.getHeight();
            g.drawImage(img, 0, 0, drawwidth, fheight, null);
        } else {
            int drawheight = fwidth * img.getHeight() / img.getWidth();
            g.drawImage(img, 0, 0, fwidth, drawheight, null);
        }
    }

    public static void drawBoxOutline(Graphics2D g, int x, int y, int width, int height) {
        g.drawImage(FrameSprites.box.getSprite(Direction.NORTHWEST), x - OUTLINE_WIDTH, y - OUTLINE_HEIGHT, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.NORTHEAST), x + width, y - OUTLINE_HEIGHT, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.SOUTHWEST), x - OUTLINE_WIDTH, y + height, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.SOUTHEAST), x + width, y + height, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.NORTH), x, y - OUTLINE_HEIGHT, width, OUTLINE_HEIGHT, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.SOUTH), x, y + height, width, OUTLINE_HEIGHT, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.WEST), x - OUTLINE_WIDTH, y, OUTLINE_WIDTH, height, null);
        g.drawImage(FrameSprites.box.getSprite(Direction.EAST), x + width, y, OUTLINE_WIDTH, height, null);
    }

}

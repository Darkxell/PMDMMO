package com.darkxell.client.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.Sprites.Res_Frame;

/**
 * Class that contains various utility methods for the statemanager to display the components on the frame.
 */
public abstract class MainUiUtility {

    private static final int OUTLINE_WIDTH = 16, OUTLINE_HEIGHT = 8;

    public static void drawBackground(Graphics2D g, int fwidth, int fheight, byte backgroundID) {
        BufferedImage img;

        img = Res_Frame.getBackground(backgroundID);

        if ((float) (fwidth) / (float) (fheight) < (float) (img.getWidth()) / (float) (img.getHeight())) {
            int drawwidth = fheight * img.getWidth() / img.getHeight();
            g.drawImage(img, 0, 0, drawwidth, fheight, null);
        } else {
            int drawheight = fwidth * img.getHeight() / img.getWidth();
            g.drawImage(img, 0, 0, fwidth, drawheight, null);
        }
    }

    public static void drawBoxOutline(Graphics2D g, int x, int y, int width, int height) {
        g.drawImage(Res_Frame.box_NW.image(), x - OUTLINE_WIDTH, y - OUTLINE_HEIGHT, OUTLINE_WIDTH, OUTLINE_HEIGHT,
                null);
        g.drawImage(Res_Frame.box_NE.image(), x + width, y - OUTLINE_HEIGHT, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(Res_Frame.box_SW.image(), x - OUTLINE_WIDTH, y + height, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(Res_Frame.box_SE.image(), x + width, y + height, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
        g.drawImage(Res_Frame.box_N.image(), x, y - OUTLINE_HEIGHT, width, OUTLINE_HEIGHT, null);
        g.drawImage(Res_Frame.box_S.image(), x, y + height, width, OUTLINE_HEIGHT, null);
        g.drawImage(Res_Frame.box_W.image(), x - OUTLINE_WIDTH, y, OUTLINE_WIDTH, height, null);
        g.drawImage(Res_Frame.box_E.image(), x + width, y, OUTLINE_WIDTH, height, null);
    }

}

package com.darkxell.client.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.others.FrameResources;

/**
 * Class that contains various utility methods for the statemanager to display
 * the components on the frame.
 */
public abstract class MainUiUtility {
	
	private static final int OUTLINE_WIDTH = 16, OUTLINE_HEIGHT = 8;

	public static void drawBackground(Graphics2D g, int fwidth, int fheight, byte backgroundID) {
		BufferedImage img;
		switch (backgroundID) {
		case 2:
			img = FrameResources.BG2.image();
			break;
		case 3:
			img = FrameResources.BG3.image();
			break;
		case 4:
			img = FrameResources.BG4.image();
			break;
		case 5:
			img = FrameResources.BG5.image();
			break;
		case 6:
			img = FrameResources.BG6.image();
			break;
		case 7:
			img = FrameResources.BG7.image();
			break;
		default:
			img = FrameResources.BG1.image();
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
		g.drawImage(FrameResources.box_NW.image(), x - OUTLINE_WIDTH, y - OUTLINE_HEIGHT, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
		g.drawImage(FrameResources.box_NE.image(), x + width, y - OUTLINE_HEIGHT, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
		g.drawImage(FrameResources.box_SW.image(), x - OUTLINE_WIDTH, y + height, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
		g.drawImage(FrameResources.box_SE.image(), x + width, y + height, OUTLINE_WIDTH, OUTLINE_HEIGHT, null);
		g.drawImage(FrameResources.box_N.image(), x, y - OUTLINE_HEIGHT, width, OUTLINE_HEIGHT, null);
		g.drawImage(FrameResources.box_S.image(), x, y + height, width, OUTLINE_HEIGHT, null);
		g.drawImage(FrameResources.box_W.image(), x - OUTLINE_WIDTH, y, OUTLINE_WIDTH, height, null);
		g.drawImage(FrameResources.box_E.image(), x + width, y, OUTLINE_WIDTH, height, null);
	}

}

package com.darkxell.client.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.FrameResources;

/**
 * Class that contains various utility methods for the statemanager to display
 * the components on the frame.
 */
public abstract class MainUiUtility {

	public static void drawBackground(Graphics2D g, int fwidth, int fheight, byte backgroundID) {
		BufferedImage img;
		switch (backgroundID) {
		case 2:
			img = FrameResources.BG2;
			break;
		case 3:
			img = FrameResources.BG3;
			break;
		case 4:
			img = FrameResources.BG4;
			break;
		case 5:
			img = FrameResources.BG5;
			break;
		case 6:
			img = FrameResources.BG6;
			break;
		case 7:
			img = FrameResources.BG7;
			break;
		default:
			img = FrameResources.BG1;
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
		g.drawImage(FrameResources.box_NW, x - 16, y - 10, 16, 10, null);
		g.drawImage(FrameResources.box_NE, x + width, y - 10, 16, 10, null);
		g.drawImage(FrameResources.box_SW, x - 16, y + height, 16, 10, null);
		g.drawImage(FrameResources.box_SE, x + width, y + height, 16, 10, null);
		g.drawImage(FrameResources.box_N, x, y - 10, width, 10, null);
		g.drawImage(FrameResources.box_S, x, y + height, width, 10, null);
		g.drawImage(FrameResources.box_W, x - 16, y, 16, height, null);
		g.drawImage(FrameResources.box_E, x + width, y, 16, height, null);
	}

}

package com.darkxell.client.state.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class AbstractDisplayMap {

	public BufferedImage canvas = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);

	/**
	 * renders this map on its own canvas, then prints the canvas to the parsed
	 * graphics object.
	 */
	public abstract void render(Graphics2D g, int width, int height);

	/** Updates this map. */
	public abstract void update();

}

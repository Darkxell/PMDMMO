package com.darkxell.client.renderers.layers;

import java.awt.Graphics2D;

/**
 * This object describes an animated background or foreground. This asset is
 * based on update/render mechanics. The render method must render on the whole
 * screen.
 */
public abstract class AbstractGraphicLayer {

	public abstract void update();

	public abstract void render(Graphics2D g, int width, int height);

}

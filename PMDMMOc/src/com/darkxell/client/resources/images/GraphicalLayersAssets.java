package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

/** Holds the differents assets related to graphical layers. */
public abstract class GraphicalLayersAssets {

	private static final BufferedImage source_WATER = Res.getBase("resources/graphicallayers/water.png");
	public static BufferedImage WATER_clouds = Res.createimage(source_WATER, 0, 0, 720, 208);
	public static BufferedImage[] WATER_horizon = Res.getSpriteRectRow(source_WATER, 0, 208, 48, 128, 5);
	public static BufferedImage[] WATER_waves = Res.getSpriteRectRow(source_WATER, 240, 208, 48, 168, 10);

}

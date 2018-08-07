package com.darkxell.client.resources.images.hud;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

/** Holds the differents assets related to graphical layers. */
public abstract class GraphicalLayersAssets {

	private static final BufferedImage source_WATER = Res.getBase("/graphicallayers/water.png");
	public static BufferedImage WATER_clouds = Res.createimage(source_WATER, 0, 0, 720, 208);
	public static BufferedImage[] WATER_horizon = Res.getSpriteRectRow(source_WATER, 0, 208, 48, 128, 5);
	public static BufferedImage[] WATER_waves = Res.getSpriteRectRow(source_WATER, 240, 208, 48, 168, 10);

	private static final BufferedImage source_LSD = Res.getBase("/graphicallayers/lsd.png");
	public static BufferedImage[] LSD_top = Res.getSpriteRectRow(source_LSD, 0, 0, 42, 160, 8);
	public static BufferedImage[] LSD_bot = Res.getSpriteRectRow(source_LSD, 0, 160, 42, 160, 8);

}

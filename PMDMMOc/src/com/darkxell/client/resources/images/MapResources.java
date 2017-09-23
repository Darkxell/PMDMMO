package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class MapResources {

	public static final BufferedImage LOCALMAP = Res.getBase("resources/hud/map/localmap.png");

	private static final BufferedImage PINSBASE = Res.getBase("resources/hud/map/pins.png");
	public static final BufferedImage PIN_RED = Res.createimage(PINSBASE, 0, 0, 12, 12);
	public static final BufferedImage PIN_YELLOW = Res.createimage(PINSBASE, 12, 0, 12, 12);
	public static final BufferedImage PIN_BLUE = Res.createimage(PINSBASE, 24, 0, 12, 12);
	public static final BufferedImage PIN_GREEN = Res.createimage(PINSBASE, 36, 0, 12, 12);

}

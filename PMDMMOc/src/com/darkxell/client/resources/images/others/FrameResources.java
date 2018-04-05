package com.darkxell.client.resources.images.others;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class FrameResources {

	public static final BufferedImage source = Res.getBase("/hud/boxcorners.png");
	public static final BufferedImage box_NW = Res.createimage(source, 0, 0, 7, 3);
	public static final BufferedImage box_NE = Res.createimage(source, 8, 0, 7, 3);
	public static final BufferedImage box_SW = Res.createimage(source, 0, 4, 7, 3);
	public static final BufferedImage box_SE = Res.createimage(source, 8, 4, 7, 3);
	public static final BufferedImage box_N = Res.createimage(source, 7, 0, 1, 3);
	public static final BufferedImage box_E = Res.createimage(source, 8, 3, 7, 1);
	public static final BufferedImage box_S = Res.createimage(source, 7, 4, 1, 3);
	public static final BufferedImage box_W = Res.createimage(source, 0, 3, 7, 1);
	
	public static final BufferedImage BG1 = Res.getBase("/hud/framebackgrounds/1.jpg");
	public static final BufferedImage BG2 = Res.getBase("/hud/framebackgrounds/2.jpg");
	public static final BufferedImage BG3 = Res.getBase("/hud/framebackgrounds/3.png");
	public static final BufferedImage BG4 = Res.getBase("/hud/framebackgrounds/4.png");
	public static final BufferedImage BG5 = Res.getBase("/hud/framebackgrounds/5.jpg");
	public static final BufferedImage BG6 = Res.getBase("/hud/framebackgrounds/6.png");
	public static final BufferedImage BG7 = Res.getBase("/hud/framebackgrounds/7.jpg");
	
	public static final BufferedImage ICON = Res.getBase("/hud/framebackgrounds/icon.png");

}

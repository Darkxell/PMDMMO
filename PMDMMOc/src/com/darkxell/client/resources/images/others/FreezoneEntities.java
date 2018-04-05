package com.darkxell.client.resources.images.others;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class FreezoneEntities {

	public static BufferedImage YellowFlowerSource = Res.getBase("/freezones/entities/yellowflower.png");
	public static BufferedImage[] YellowFlower = Res.getSpriteRow(YellowFlowerSource, 0, 0, 32, 6);
	public static BufferedImage RedFlowerSource = Res.getBase("/freezones/entities/redflower.png");
	public static BufferedImage[] RedFlower = Res.getSpriteRow(RedFlowerSource, 0, 0, 32, 6);
	public static BufferedImage flagsource = Res.getBase("/freezones/entities/flag.png");
	public static BufferedImage[] flag = Res.getSpriteRectRow(flagsource, 0, 0, 32, 8, 6);

	public static BufferedImage watersource = Res.getBase("/freezones/entities/watersparkles.png");
	public static BufferedImage[] watersparkles_side = Res.getSpriteRectRow(watersource, 0, 0, 19, 41, 6);
	public static BufferedImage[] watersparkles_bot = Res.getSpriteRectRow(watersource, 0, 42, 19, 29, 6);
	public static BufferedImage[] watersparkles_top = Res.getSpriteRectRow(watersource, 0, 72, 19, 23, 6);
	public static BufferedImage[] watersparkles_long = Res.getSpriteRectRow(watersource, 0, 96, 32, 8, 6);
}

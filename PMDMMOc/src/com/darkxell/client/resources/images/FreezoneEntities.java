package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class FreezoneEntities {

	public static BufferedImage YellowFlowerSource = Res.getBase("resources/freezones/entities/yellowflower.png");
	public static BufferedImage[] YellowFlower = Res.getSpriteRow(YellowFlowerSource, 0, 0, 32, 6);
	public static BufferedImage RedFlowerSource = Res.getBase("resources/freezones/entities/redflower.png");
	public static BufferedImage[] RedFlower = Res.getSpriteRow(RedFlowerSource, 0, 0, 32, 6);
	public static BufferedImage flagsource = Res.getBase("resources/freezones/entities/flag.png");
	public static BufferedImage[] flag = Res.getSpriteRectRow(flagsource, 0, 0, 32, 8, 6);

}

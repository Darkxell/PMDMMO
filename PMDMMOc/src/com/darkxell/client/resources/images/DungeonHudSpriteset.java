package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.GameUtil;

public class DungeonHudSpriteset
{

	public static final DungeonHudSpriteset instance = new DungeonHudSpriteset();

	private BufferedImage[] arrows;

	public DungeonHudSpriteset()
	{
		BufferedImage source = Res.getBase("resources/hud/dungeon_hud.png");
		this.arrows = new BufferedImage[8];
		this.arrows[0] = Res.createimage(source, 13, 1, 8, 5);
		this.arrows[1] = Res.createimage(source, 6, 0, 6, 6);
		this.arrows[2] = Res.createimage(source, 27, 2, 5, 8);
		this.arrows[3] = Res.createimage(source, 6, 6, 6, 6);
		this.arrows[4] = Res.createimage(source, 13, 6, 8, 5);
		this.arrows[5] = Res.createimage(source, 0, 6, 6, 6);
		this.arrows[6] = Res.createimage(source, 22, 2, 5, 8);
		this.arrows[7] = Res.createimage(source, 0, 0, 6, 6);
	}

	/** @return The arrow pointing in the input direction. */
	public BufferedImage getArrow(short direction)
	{
		return this.arrows[GameUtil.indexOf(direction)];
	}

}

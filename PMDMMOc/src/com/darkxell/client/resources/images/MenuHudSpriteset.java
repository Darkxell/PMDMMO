package com.darkxell.client.resources.images;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.GameUtil;

public class MenuHudSpriteset
{

	public static final MenuHudSpriteset instance = new MenuHudSpriteset();

	public final Dimension cornerSize, cornerNameSize;
	private BufferedImage[] sprites;

	public MenuHudSpriteset()
	{
		BufferedImage source = Res.getBase("resources/hud/menu_hud.png");
		this.sprites = new BufferedImage[13];
		this.sprites[0] = Res.createimage(source, 0, 0, 10, 16);
		this.sprites[1] = Res.createimage(source, 12, 0, 10, 16);
		this.sprites[2] = Res.createimage(source, 24, 0, 8, 8);
		this.sprites[3] = Res.createimage(source, 24, 8, 8, 8);
		this.sprites[4] = Res.createimage(source, 0, 16, 16, 10);
		this.sprites[5] = Res.createimage(source, 16, 16, 16, 10);
		this.sprites[6] = Res.createimage(source, 0, 26, 16, 10);
		this.sprites[7] = Res.createimage(source, 16, 26, 16, 10);
		this.sprites[8] = Res.createimage(source, 0, 36, 14, 10);
		this.sprites[9] = Res.createimage(source, 18, 36, 14, 10);
		this.sprites[10] = Res.createimage(source, 0, 46, 14, 10);
		this.sprites[11] = Res.createimage(source, 18, 46, 14, 10);
		this.sprites[12] = Res.createimage(source, 0, 56, 11, 7);
		this.cornerSize = new Dimension(this.sprites[4].getWidth(), this.sprites[4].getHeight());
		this.cornerNameSize = new Dimension(this.sprites[8].getWidth(), this.sprites[8].getHeight());
	}

	public BufferedImage nextWindowArrow()
	{
		return this.sprites[12];
	}

	public BufferedImage selectedArrow()
	{
		return this.sprites[1];
	}

	public BufferedImage selectionArrow()
	{
		return this.sprites[0];
	}

	public BufferedImage tabArrowLeft()
	{
		return this.sprites[2];
	}

	public BufferedImage tabArrowRight()
	{
		return this.sprites[3];
	}

	public BufferedImage windowCorner(short direction)
	{
		switch (direction)
		{
			case GameUtil.NORTHEAST:
				return this.sprites[5];

			case GameUtil.SOUTHEAST:
				return this.sprites[7];

			case GameUtil.SOUTHWEST:
				return this.sprites[6];

			default:
				return this.sprites[4];
		}
	}

	public BufferedImage windowNameCorner(short direction)
	{
		switch (direction)
		{
			case GameUtil.NORTHEAST:
				return this.sprites[9];

			case GameUtil.SOUTHEAST:
				return this.sprites[11];

			case GameUtil.SOUTHWEST:
				return this.sprites[10];

			default:
				return this.sprites[8];
		}
	}

}

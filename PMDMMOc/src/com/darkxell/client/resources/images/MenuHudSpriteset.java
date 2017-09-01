package com.darkxell.client.resources.images;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.GameUtil;

public class MenuHudSpriteset
{

	public static final MenuHudSpriteset instance = new MenuHudSpriteset();

	public final Dimension cornerSize;
	private BufferedImage[] sprites;

	public MenuHudSpriteset()
	{
		BufferedImage source = Res.getBase("resources/hud/menu_hud.png");
		this.sprites = new BufferedImage[8];
		this.sprites[0] = Res.createimage(source, 0, 0, 10, 16);
		this.sprites[1] = Res.createimage(source, 16, 0, 10, 16);
		this.sprites[2] = Res.createimage(source, 32, 0, 16, 16);
		this.sprites[3] = Res.createimage(source, 48, 0, 16, 16);
		this.sprites[4] = Res.createimage(source, 0, 16, 16, 8);
		this.sprites[5] = Res.createimage(source, 16, 16, 16, 8);
		this.sprites[6] = Res.createimage(source, 0, 24, 16, 8);
		this.sprites[7] = Res.createimage(source, 16, 24, 16, 8);
		this.cornerSize = new Dimension(this.sprites[4].getWidth(), this.sprites[4].getHeight());
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
		return this.sprites[2];
	}

	public BufferedImage windowCorner(short direction)
	{
		switch (direction)
		{
			case GameUtil.NORTHEAST:
				return this.sprites[5];

			case GameUtil.SOUTHEAST:
				return this.sprites[6];

			case GameUtil.SOUTHWEST:
				return this.sprites[7];

			default:
				return this.sprites[4];
		}
	}

}

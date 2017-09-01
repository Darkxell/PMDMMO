package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class MenuHudSpriteset
{

	public static final MenuHudSpriteset instance = new MenuHudSpriteset();

	private BufferedImage[] sprites;

	public MenuHudSpriteset()
	{
		BufferedImage source = Res.getBase("resources/hud/menu_hud.png");
		this.sprites = new BufferedImage[4];
		this.sprites[0] = Res.createimage(source, 0, 0, 5, 8);
		this.sprites[1] = Res.createimage(source, 8, 0, 5, 8);
		this.sprites[2] = Res.createimage(source, 0, 8, 8, 8);
		this.sprites[3] = Res.createimage(source, 8, 8, 8, 8);
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

}

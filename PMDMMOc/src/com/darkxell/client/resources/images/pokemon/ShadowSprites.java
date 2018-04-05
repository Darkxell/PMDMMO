package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class ShadowSprites
{

	public static final ShadowSprites instance = new ShadowSprites();

	private BufferedImage[] sprites;

	public ShadowSprites()
	{
		BufferedImage source = Res.getBase("/pokemons/shadows.png");
		this.sprites = new BufferedImage[8];
		this.sprites[0] = Res.createimage(source, 0, 0, 14, 6);
		this.sprites[1] = Res.createimage(source, 0, 6, 14, 6);
		this.sprites[2] = Res.createimage(source, 0, 12, 14, 6);
		this.sprites[3] = Res.createimage(source, 0, 18, 14, 6);
		this.sprites[4] = Res.createimage(source, 0, 24, 20, 10);
		this.sprites[5] = Res.createimage(source, 0, 34, 20, 10);
		this.sprites[6] = Res.createimage(source, 0, 44, 20, 10);
		this.sprites[7] = Res.createimage(source, 0, 54, 20, 10);
	}

	public BufferedImage getBig(byte shadowColor)
	{
		return this.sprites[4 + shadowColor];
	}

	public BufferedImage getSmall(byte shadowColor)
	{
		return this.sprites[shadowColor];
	}

}

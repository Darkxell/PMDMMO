package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.darkxell.client.resources.Res;

public class AnimationSpriteset
{

	private static final HashMap<String, AnimationSpriteset> spritesets = new HashMap<String, AnimationSpriteset>();

	public static AnimationSpriteset getSpriteset(String path, int width, int height)
	{
		if (!spritesets.containsKey(path)) spritesets.put(path, new AnimationSpriteset(path, width, height));
		return spritesets.get(path);
	}

	private BufferedImage[] sprites;
	public final int spriteWidth, spriteHeight;

	private AnimationSpriteset(String path, int width, int height)
	{
		this.spriteWidth = width;
		this.spriteHeight = height;
		BufferedImage source = Res.getBase(path);
		int rows, cols;
		cols = source.getWidth() / this.spriteWidth;
		rows = source.getHeight() / this.spriteHeight;
		this.sprites = new BufferedImage[cols * rows];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.sprites[(cols * i) + j] = Res.createimage(source, this.spriteWidth * j, this.spriteHeight * i, this.spriteWidth, this.spriteHeight);
	}

	public BufferedImage getSprite(int id)
	{
		if (id < 0 || id > this.sprites.length) return null;
		return this.sprites[id];
	}

	public int spriteCount()
	{
		return this.sprites.length;
	}

}

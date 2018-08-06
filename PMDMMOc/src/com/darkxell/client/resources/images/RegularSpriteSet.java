package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

/** Represents a Spriteset where each sprite has a fixed width and height. */
public class RegularSpriteSet extends SpriteSet
{

	public final int columns, rows;
	public final int spriteWidth, spriteHeight;

	public RegularSpriteSet(String path, int spriteSize, int width, int height)
	{
		this(path, spriteSize, spriteSize, width, height);
	}

	public RegularSpriteSet(String path, int spriteWidth, int spriteHeight, int width, int height)
	{
		super(path, width, height);
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;

		this.columns = this.image().getWidth() / this.spriteWidth;
		this.rows = this.image().getHeight() / this.spriteHeight;
		for (int x = 0; x < this.columns; ++x)
			for (int y = 0; y < this.rows; ++y)
				this.createSprite(x + "," + y, this.spriteWidth * x, this.spriteHeight * y, this.spriteWidth, this.spriteHeight);
	}

	/** @return The Sprite at the input x, y coordinates. */
	public Sprite get(int x, int y)
	{
		if (x < 0 || y < 0 || x >= this.columns || y >= this.rows) return null;
		return this.get(x + "," + y);
	}

	/** @return The Image of the Sprite at the input x, y coordinates. */
	public BufferedImage getImg(int x, int y)
	{
		Sprite s = this.get(x, y);
		if (s == null) return null;
		return s.image();
	}

}

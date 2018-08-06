package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

/** Represents a Spriteset where each sprite has a fixed width and height. */
public class RegularSpriteSet extends SpriteSet
{

	private int columns = -1, rows = -1;
	private BufferedImage defaultSprite;
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
		this.defaultSprite = SpriteFactory.instance().getDefault(spriteWidth, spriteHeight);
		if (width != -1 && height != -1) this.onLoad();
	}

	/** @return The number of columns in this SpriteSet. May return -1 if it isn't loaded yet. */
	public int columns()
	{
		return this.columns;
	}

	/** @return The Sprite at the input position, where pos = x + y * cols. */
	Sprite get(int pos)
	{
		return this.get(pos % this.rows, pos / this.rows);
	}

	/** @return The Sprite at the input x, y coordinates. */
	Sprite get(int x, int y)
	{
		if (x < 0 || y < 0 || x >= this.columns || y >= this.rows) return this.get("0,0");
		return this.get(x + "," + y);
	}

	/** @return The Image of the Sprite at the input position, where pos = x + y * cols. */
	public BufferedImage getImg(int pos)
	{
		Sprite s = this.get(pos);
		if (s == null) return this.defaultSprite;
		return s.image();
	}

	/** @return The Image of the Sprite at the input x, y coordinates. */
	public BufferedImage getImg(int x, int y)
	{
		Sprite s = this.get(x, y);
		if (s == null) return this.defaultSprite;
		return s.image();
	}

	@Override
	protected void loaded(BufferedImage img)
	{
		super.loaded(img);
		if (this.columns == -1 || this.rows == -1) this.onLoad();
	}

	private void onLoad()
	{
		this.columns = this.image().getWidth() / this.spriteWidth;
		this.rows = this.image().getHeight() / this.spriteHeight;
		for (int x = 0; x < this.columns; ++x)
			for (int y = 0; y < this.rows; ++y)
				this.createSprite(x + "," + y, this.spriteWidth * x, this.spriteHeight * y, this.spriteWidth, this.spriteHeight);
	}

	/** @return The number of rows in this SpriteSet. May return -1 if it isn't loaded yet. */
	public int rows()
	{
		return this.rows;
	}

}

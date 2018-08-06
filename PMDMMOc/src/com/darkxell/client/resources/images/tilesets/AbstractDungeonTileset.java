package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.RegularSpriteSet;

public abstract class AbstractDungeonTileset extends RegularSpriteSet
{

	public static final int TILE_SIZE = 24;

	public AbstractDungeonTileset(String path, int width, int height)
	{
		super(path, TILE_SIZE, width, height);
	}

	/** @return The tile at the given x, y coordinates. */
	public BufferedImage tileAt(int x, int y)
	{
		if (x >= 0 && x < this.columns() && y >= 0 && y < this.rows()) return this.getImg(x, y);
		return this.getImg(0, 0);
	}

}

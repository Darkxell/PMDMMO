package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public abstract class AbstractDungeonTileset
{

	public static final int TILE_SIZE = 24;

	/** The tileset sprites. For specific IDs, refer to AbstractTileset.ID_[...] */
	private final BufferedImage[][] tiles;

	public AbstractDungeonTileset(String path)
	{
		BufferedImage source = Res.getBase(path);
		int rows, cols;
		cols = source.getWidth() / TILE_SIZE;
		rows = source.getHeight() / TILE_SIZE;
		this.tiles = new BufferedImage[cols][rows];
		for (int x = 0; x < cols; ++x)
			for (int y = 0; y < rows; ++y)
				this.tiles[x][y] = Res.createimage(source, TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
	}

	/** @return The tile at the given x, y coordinates. */
	public BufferedImage tileAt(int x, int y)
	{
		if (x >= 0 && x < this.tiles.length && y >= 0 && y < this.tiles[x].length) return this.tiles[x][y];
		return this.tiles[0][0];
	}

}

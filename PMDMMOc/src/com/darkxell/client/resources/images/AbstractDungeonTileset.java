package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class AbstractDungeonTileset
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
		for (int x = 0; x < rows; ++x)
			for (int y = 0; y < cols; ++y)
				this.tiles[x][y] = Res.createimage(source, TILE_SIZE * y, TILE_SIZE * x, TILE_SIZE, TILE_SIZE);
	}

	/** @return The tile at the given x, y coordinates. */
	public BufferedImage tileAt(int x, int y)
	{
		return this.tiles[x][y];
	}

}

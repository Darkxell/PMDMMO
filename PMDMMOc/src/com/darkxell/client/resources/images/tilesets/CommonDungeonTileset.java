package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.AbstractDungeonTileset;

public class CommonDungeonTileset extends AbstractDungeonTileset
{

	public static final CommonDungeonTileset INSTANCE = new CommonDungeonTileset();

	public CommonDungeonTileset()
	{
		super("resources/tilesets/dungeon-common.png");
	}

	/** @param facing - true if the Pokémon is facing this Tile (red grid), false else (yellow grid).
	 * @return The grid tile. */
	public BufferedImage grid(boolean facing)
	{
		return this.tileAt(facing ? 5 : 4, 0);
	}

	public BufferedImage rescue()
	{
		return this.tileAt(0, 1);
	}

	public BufferedImage shop()
	{
		return this.tileAt(3, 0);
	}

	/** @param up - true if the stairs are going up, false if they're going down.
	 * @return The stairs tile. */
	public BufferedImage stairs(boolean up)
	{
		return this.tileAt(up ? 1 : 0, 1);
	}

	/** @param id - The Trap's ID.
	 * @return A Trap tile. */
	public BufferedImage trap(int id)
	{
		int x = id + 7, y = 0;
		while (x > 5)
		{
			x -= 6;
			++y;
		}
		return this.tileAt(x, y);
	}

	public BufferedImage warp()
	{
		return this.tileAt(1, 1);
	}

	public BufferedImage wonderTile()
	{
		return this.tileAt(2, 0);
	}

}

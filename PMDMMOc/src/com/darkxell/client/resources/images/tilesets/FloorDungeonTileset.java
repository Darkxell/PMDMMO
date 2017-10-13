package com.darkxell.client.resources.images.tilesets;

import static com.darkxell.common.util.Directions.*;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

public class FloorDungeonTileset extends AbstractDungeonTileset
{

	/** The last tileset to be used. */
	private static FloorDungeonTileset previous = null;
	private static final HashMap<Integer, Point> tileLocations = new HashMap<Integer, Point>();

	static
	{
		// Single
		tileLocations.put((int) NORTH, new Point(1, 8));
		tileLocations.put((int) EAST, new Point(2, 7));
		tileLocations.put((int) SOUTH, new Point(1, 6));
		tileLocations.put((int) WEST, new Point(0, 7));

		// Two
		tileLocations.put(NORTH + SOUTH, new Point(0, 4));
		tileLocations.put(NORTH + EAST, new Point(0, 5));
		tileLocations.put(NORTH + WEST, new Point(2, 5));
		tileLocations.put(SOUTH + EAST, new Point(0, 3));
		tileLocations.put(SOUTH + WEST, new Point(2, 3));
		tileLocations.put(EAST + WEST, new Point(1, 3));

		// Corner
		tileLocations.put(NORTH + EAST + NORTHEAST, new Point(0, 2));
		tileLocations.put(SOUTH + EAST + SOUTHEAST, new Point(0, 0));
		tileLocations.put(NORTH + WEST + NORTHWEST, new Point(2, 2));
		tileLocations.put(SOUTH + WEST + SOUTHWEST, new Point(2, 0));

		// T-shape
		tileLocations.put(NORTH + EAST + SOUTH, new Point(0, 10));
		tileLocations.put(NORTH + WEST + SOUTH, new Point(2, 10));
		tileLocations.put(NORTH + EAST + WEST, new Point(1, 11));
		tileLocations.put(SOUTH + EAST + WEST, new Point(1, 9));

		// T with one corner
		tileLocations.put(NORTH + EAST + SOUTH + NORTHEAST, new Point(0, 17));
		tileLocations.put(NORTH + EAST + SOUTH + SOUTHEAST, new Point(0, 18));
		tileLocations.put(NORTH + WEST + SOUTH + NORTHWEST, new Point(1, 17));
		tileLocations.put(NORTH + WEST + SOUTH + SOUTHWEST, new Point(1, 18));
		tileLocations.put(SOUTH + EAST + WEST + SOUTHWEST, new Point(0, 19));
		tileLocations.put(SOUTH + EAST + WEST + SOUTHEAST, new Point(1, 19));
		tileLocations.put(NORTH + EAST + WEST + NORTHWEST, new Point(0, 20));
		tileLocations.put(NORTH + EAST + WEST + NORTHEAST, new Point(1, 20));

		// T with two corners
		tileLocations.put(NORTH + EAST + SOUTH + NORTHEAST + SOUTHEAST, new Point(0, 1));
		tileLocations.put(NORTH + WEST + SOUTH + NORTHWEST + SOUTHWEST, new Point(2, 1));
		tileLocations.put(SOUTH + EAST + WEST + SOUTHWEST + SOUTHEAST, new Point(1, 0));
		tileLocations.put(NORTH + EAST + WEST + NORTHWEST + NORTHEAST, new Point(1, 2));

		// Cross
		tileLocations.put(NORTH + NORTHEAST + EAST + SOUTHEAST + SOUTH + SOUTHWEST + WEST + NORTHWEST, new Point(1, 1));
		tileLocations.put(NORTH + EAST + SOUTH + WEST, new Point(1, 7));

		// Cross, one corner
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHEAST, new Point(0, 22));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHWEST, new Point(1, 22));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHEAST, new Point(0, 23));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHWEST, new Point(1, 23));

		// Cross, two corners
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHWEST + NORTHEAST, new Point(1, 12));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHWEST + SOUTHEAST, new Point(1, 14));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHWEST + SOUTHWEST, new Point(0, 13));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHEAST + SOUTHEAST, new Point(2, 13));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHWEST + NORTHEAST, new Point(0, 24));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHEAST + NORTHWEST, new Point(1, 24));

		// Cross, three corners
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHWEST + NORTHEAST + SOUTHWEST, new Point(0, 15));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHEAST + NORTHWEST + NORTHEAST, new Point(1, 15));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + SOUTHWEST + SOUTHEAST + NORTHWEST, new Point(0, 16));
		tileLocations.put(NORTH + EAST + SOUTH + WEST + NORTHEAST + SOUTHWEST + SOUTHEAST, new Point(1, 16));
	}

	public static FloorDungeonTileset load(int id)
	{
		if (previous != null && previous.id == id) return previous;
		if (new File("resources/tilesets/dungeon/dungeon-" + id + ".png").exists()) return previous = new FloorDungeonTileset(id,
				"resources/tilesets/dungeon/dungeon-" + id + ".png");
		return previous = new FloorDungeonTileset(id, "resources/tilesets/dungeon/dungeon-0.png");
	}

	public final int id;

	public FloorDungeonTileset(int id, String path)
	{
		super(path);
		this.id = id;
	}

	public BufferedImage defaultTile()
	{
		return this.tileAt(1, 1);
	}

	public BufferedImage tile(Tile tile)
	{
		int x = 0, y = 0;
		if (tile.type() == TileType.GROUND) x = 3 * 3;
		else if (tile.type() == TileType.WATER || tile.type() == TileType.LAVA || tile.type() == TileType.AIR) x = 5 * 3;

		Point p = tileLocations.get((int) tile.getNeighbors());
		if (p == null)
		{
			x += 1;
			y += 4;
		} else
		{
			x += p.x;
			y += p.y;
		}

		x += tile.alternate * 3;

		return this.tileAt(x, y);
	}
}

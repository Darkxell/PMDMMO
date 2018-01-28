package com.darkxell.client.resources.images.tilesets;

import static com.darkxell.common.util.Direction.EAST;
import static com.darkxell.common.util.Direction.NORTH;
import static com.darkxell.common.util.Direction.NORTHEAST;
import static com.darkxell.common.util.Direction.NORTHWEST;
import static com.darkxell.common.util.Direction.SOUTH;
import static com.darkxell.common.util.Direction.SOUTHEAST;
import static com.darkxell.common.util.Direction.SOUTHWEST;
import static com.darkxell.common.util.Direction.WEST;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DirectionSet;

public class FloorDungeonTileset extends AbstractDungeonTileset
{

	/** The last tileset to be used. */
	private static FloorDungeonTileset previous = null;
	private static final HashMap<DirectionSet, Point> tileLocations = new HashMap<>();

	static
	{
		// Single
		register(new Point(1, 8), NORTH);
		register(new Point(2, 7), EAST);
		register(new Point(1, 6), SOUTH);
		register(new Point(0, 7), WEST);

		// Two
		register(new Point(0, 4), NORTH, SOUTH);
		register(new Point(0, 5), NORTH, EAST);
		register(new Point(2, 5), NORTH, WEST);
		register(new Point(0, 3), SOUTH, EAST);
		register(new Point(2, 3), SOUTH, WEST);
		register(new Point(1, 3), EAST, WEST);

		// Corner
		register(new Point(0, 2), NORTH, EAST, NORTHEAST);
		register(new Point(0, 0), SOUTH, EAST, SOUTHEAST);
		register(new Point(2, 2), NORTH, WEST, NORTHWEST);
		register(new Point(2, 0), SOUTH, WEST, SOUTHWEST);

		// T-shape
		register(new Point(0, 10), NORTH, EAST, SOUTH);
		register(new Point(2, 10), NORTH, WEST, SOUTH);
		register(new Point(1, 11), NORTH, EAST, WEST);
		register(new Point(1, 9), SOUTH, EAST, WEST);

		// T with one corner
		register(new Point(0, 17), NORTH, EAST, SOUTH, NORTHEAST);
		register(new Point(0, 18), NORTH, EAST, SOUTH, SOUTHEAST);
		register(new Point(1, 17), NORTH, WEST, SOUTH, NORTHWEST);
		register(new Point(1, 18), NORTH, WEST, SOUTH, SOUTHWEST);
		register(new Point(0, 19), SOUTH, EAST, WEST, SOUTHWEST);
		register(new Point(1, 19), SOUTH, EAST, WEST, SOUTHEAST);
		register(new Point(0, 20), NORTH, EAST, WEST, NORTHWEST);
		register(new Point(1, 20), NORTH, EAST, WEST, NORTHEAST);

		// T with two corners
		register(new Point(0, 1), NORTH, EAST, SOUTH, NORTHEAST, SOUTHEAST);
		register(new Point(2, 1), NORTH, WEST, SOUTH, NORTHWEST, SOUTHWEST);
		register(new Point(1, 0), SOUTH, EAST, WEST, SOUTHWEST, SOUTHEAST);
		register(new Point(1, 2), NORTH, EAST, WEST, NORTHWEST, NORTHEAST);

		// Cross
		register(new Point(1, 1), NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST);
		register(new Point(1, 7), NORTH, EAST, SOUTH, WEST);

		// Cross, one corner
		register(new Point(0, 22), NORTH, EAST, SOUTH, WEST, SOUTHEAST);
		register(new Point(1, 22), NORTH, EAST, SOUTH, WEST, SOUTHWEST);
		register(new Point(0, 23), NORTH, EAST, SOUTH, WEST, NORTHEAST);
		register(new Point(1, 23), NORTH, EAST, SOUTH, WEST, NORTHWEST);

		// Cross, two corners
		register(new Point(1, 12), NORTH, EAST, SOUTH, WEST, NORTHWEST, NORTHEAST);
		register(new Point(1, 14), NORTH, EAST, SOUTH, WEST, SOUTHWEST, SOUTHEAST);
		register(new Point(0, 13), NORTH, EAST, SOUTH, WEST, NORTHWEST, SOUTHWEST);
		register(new Point(2, 13), NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST);
		register(new Point(0, 24), NORTH, EAST, SOUTH, WEST, SOUTHWEST, NORTHEAST);
		register(new Point(1, 24), NORTH, EAST, SOUTH, WEST, SOUTHEAST, NORTHWEST);

		// Cross, three corners
		register(new Point(0, 15), NORTH, EAST, SOUTH, WEST, NORTHWEST, NORTHEAST, SOUTHWEST);
		register(new Point(1, 15), NORTH, EAST, SOUTH, WEST, SOUTHEAST, NORTHWEST, NORTHEAST);
		register(new Point(0, 16), NORTH, EAST, SOUTH, WEST, SOUTHWEST, SOUTHEAST, NORTHWEST);
		register(new Point(1, 16), NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHWEST, SOUTHEAST);
	}

	public static FloorDungeonTileset load(int id)
	{
		if (previous != null && previous.id == id) return previous;
		if (new File("resources/tilesets/dungeon/dungeon-" + id + ".png").exists())
			return previous = new FloorDungeonTileset(id, "resources/tilesets/dungeon/dungeon-" + id + ".png");
		return previous = new FloorDungeonTileset(id, "resources/tilesets/dungeon/dungeon-0.png");
	}

	private static void register(Point p, Direction... directions)
	{
		tileLocations.put(new DirectionSet(directions), p);
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

		Point p = tileLocations.get(tile.getNeighbors());
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

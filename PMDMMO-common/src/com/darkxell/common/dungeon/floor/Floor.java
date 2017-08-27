package com.darkxell.common.dungeon.floor;

import java.awt.Rectangle;
import java.util.Random;

import javafx.util.Pair;

import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.floor.layout.Layout;

/** Represents a generated Floor in a Dungeon. */
public class Floor
{

	/** Number of walkable tiles in a Floor. */
	public static final int MAX_WIDTH = 50, MAX_HEIGHT = 28;
	/** Number of unbreakable walls on each side of the map. */
	public static final int UNBREAKABLE = 10;
	/** Number of tiles, including unaccessible walls. */
	public static final int ALL_WIDTH = MAX_WIDTH + UNBREAKABLE * 2, ALL_HEIGHT = MAX_HEIGHT + UNBREAKABLE * 2;

	/** Maximum walkable coordinates in a Floor. */
	public static final Rectangle WALKABLE = new Rectangle(UNBREAKABLE, UNBREAKABLE, MAX_WIDTH, MAX_HEIGHT);

	/** This Floor's Dungeon. */
	public final Dungeon dungeon;
	/** This Floor's ID. */
	public final int id;
	/** This Floor's layout. */
	public final Layout layout;
	/** RNG */
	public final Random random;
	/** This Floor's rooms. null before generating. */
	private Room[] rooms;
	/** This Floor's tiles. null before generating. */
	private Tile[][] tiles;

	public Floor(int id, Layout layout, Dungeon dungeon)
	{
		this.id = id;
		this.dungeon = dungeon;
		this.layout = layout;
		this.random = new Random();
	}

	/** Generates this Floor. */
	public void generate()
	{
		Pair<Tile[][], Room[]> floor = this.layout.generate(this);
		this.tiles = floor.getKey();
		this.rooms = floor.getValue();
	}

	/** @return A random Room in this Floor. */
	public Room randomRoom(Random random)
	{
		return this.rooms[random.nextInt(this.rooms.length)];
	}

	/** @return The room at the input X, Y coordinates. null if not in a Room. */
	public Room roomAt(int x, int y)
	{
		for (Room room : this.rooms)
			if (room.contains(x, y)) return room;
		return null;
	}

	/** @return The tile at the input X, Y coordinates. */
	public Tile tileAt(int x, int y)
	{
		return this.tiles[x][y];
	}

	@Override
	public String toString()
	{
		String s = "";
		for (int y = 0; y < ALL_HEIGHT; ++y)
		{
			for (int x = 0; x < ALL_WIDTH; ++x)
				s += this.tileAt(x, y).type().c;
			s += "\n";
		}
		return s;
	}
}

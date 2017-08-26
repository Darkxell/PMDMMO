package com.darkxell.common.dungeon.floor;

import java.util.Random;

import javafx.util.Pair;

import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.floor.layout.Layout;

/** Represents a generated Floor in a Dungeon. */
public class Floor
{

	/** Number of tiles, including unaccessible walls. */
	public static final int ALL_WIDTH = 70, ALL_HEIGHT = 48;
	/** Number of walkable tiles in a Floor. */
	public static final int MAX_WIDTH = 50, MAX_HEIGHT = 28;
	/** Maximum walkable coordinates in a Floor. */
	public static final int MIN_X = 11, MAX_X = 59, MIN_Y = 11, MAX_Y = 39;

	/** This Floor's Dungeon. */
	public final Dungeon dungeon;
	/** This Floor's ID. */
	public final int id;
	/** This Floor's layout. */
	public final Layout layout;
	/** RNG */
	public final Random random;
	/** This Floor's rooms. */
	private final Room[] rooms;
	/** This Floor's tiles. */
	private final Tile[][] tiles;

	public Floor(int id, Layout layout, Dungeon dungeon)
	{
		this.id = id;
		this.dungeon = dungeon;
		this.layout = layout;

		Pair<Tile[][], Room[]> floor = this.layout.generate(this);
		this.tiles = floor.getKey();
		this.rooms = floor.getValue();

		this.random = new Random();
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

}

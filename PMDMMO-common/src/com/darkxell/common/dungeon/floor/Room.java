package com.darkxell.common.dungeon.floor;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

/** Represents a Room in a Floor. */
public class Room
{
	/** The floor of this Room. */
	public final Floor floor;
	/** True if this Room is a Monster House. */
	public final boolean isMonsterHouse;
	/** This this's dimensions. */
	public final int width, height;
	/** This Room's location. */
	public final int x, y;

	public Room(Floor floor, int x, int y, int width, int height, boolean isMonsterHouse)
	{
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isMonsterHouse = isMonsterHouse;
	}

	/** @return True if the input coordinates are inside this Room. */
	public boolean contains(int x, int y)
	{
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
	}

	/** @return All tiles in this Room. */
	public ArrayList<Tile> listTiles()
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int y = this.y; y <= this.y + this.height; ++y)
			for (int x = this.x; x <= this.x + this.width; ++x)
				tiles.add(this.floor.tileAt(x, y));
		return tiles;
	}

	/** @return A random tile in this Room. */
	public Tile randomTile(Random random)
	{
		ArrayList<Tile> candidates = this.listTiles();
		return candidates.get(random.nextInt(candidates.size()));
	}

	/** @return A random tile in this Room matching the input Tile type. */
	public Tile randomTile(Random random, TileType type)
	{
		ArrayList<Tile> candidates = this.listTiles();
		candidates.removeIf(new Predicate<Tile>()
		{
			@Override
			public boolean test(Tile tile)
			{
				return tile.type() != type;
			}
		});
		return candidates.get(random.nextInt(candidates.size()));
	}
}

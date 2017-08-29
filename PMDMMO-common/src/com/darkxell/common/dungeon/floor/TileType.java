package com.darkxell.common.dungeon.floor;

import java.util.ArrayList;

public enum TileType
{

	GROUND(0, ' '),
	WALL(1, 'M'),
	WALL_END(2, 'm'),
	WATER(3, 'L'),
	LAVA(4, 'L'),
	AIR(5, 'L'),
	STAIR(6, 'S'),
	WONDER_TILE(7, 'W'),
	TRAP(8, 'X'),
	WARP_ZONE(9, 'X');

	@SuppressWarnings("unchecked")
	private static ArrayList<TileType>[] tileGroups = new ArrayList[]
	{ new ArrayList<TileType>(), new ArrayList<TileType>() };
	static
	{
		tileGroups[0].add(WALL);
		tileGroups[0].add(WALL_END);

		tileGroups[1].add(GROUND);
		tileGroups[1].add(STAIR);
		tileGroups[1].add(WONDER_TILE);
		tileGroups[1].add(TRAP);
		tileGroups[1].add(WARP_ZONE);
	}

	/** @return The Tile type with the input character. */
	public static TileType find(char c)
	{
		for (TileType type : values())
			if (type.c == c) return type;
		return null;
	}

	/** @return The Tile type with the input id. */
	public static TileType find(int id)
	{
		for (TileType type : values())
			if (type.id == id) return type;
		return null;
	}

	/** Character for debug purpuses. */
	public final char c;
	public final int id;

	private TileType(int id, char c)
	{
		this.id = id;
		this.c = c;
	}

	/** @return True if this Tile connects to the input Tile. */
	public boolean connectsTo(TileType type)
	{
		for (ArrayList<TileType> group : tileGroups)
			if (group.contains(this) && group.contains(type)) return true;
		return false;
	}

}

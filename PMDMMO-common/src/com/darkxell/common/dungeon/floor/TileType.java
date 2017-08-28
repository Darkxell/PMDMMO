package com.darkxell.common.dungeon.floor;

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

}

package com.darkxell.common.dungeon.floor;

public enum TileType
{

	GROUND(0),
	WALL(1),
	WALL_END(2),
	WATER(3),
	LAVA(4),
	AIR(5),
	STAIR(6),
	WONDER_TILE(7),
	TRAP(8),
	WARP_ZONE(9);

	public final int id;

	private TileType(int id)
	{
		this.id = id;
	}

}

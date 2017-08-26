package com.darkxell.common.dungeon.floor;

/** Represents a Room in a Floor. */
public class Room
{
	/** True if this Room is a Monster House. */
	public final boolean isMonsterHouse;
	/** This room's dimensions. */
	public final int width, height;
	/** This Room's location. */
	public final int x, y;

	public Room(int x, int y, int width, int height, boolean isMonsterHouse)
	{
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
}

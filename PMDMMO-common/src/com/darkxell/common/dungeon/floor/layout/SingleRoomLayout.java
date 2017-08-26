package com.darkxell.common.dungeon.floor.layout;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;

/** A single room using the hole floor, always a Monster house. */
public class SingleRoomLayout extends Layout
{

	public SingleRoomLayout()
	{
		super(2);
	}

	@Override
	protected void generateLiquids()
	{}

	@Override
	protected void generatePaths()
	{}

	@Override
	protected Room[] generateRooms()
	{
		return new Room[]
		{ new Room(this.floor, Floor.MIN_X + 1, Floor.MIN_Y + 1, Floor.MAX_X - 1, Floor.MAX_Y - 1, true) };
	}

}

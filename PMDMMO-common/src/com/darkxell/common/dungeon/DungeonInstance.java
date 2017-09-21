package com.darkxell.common.dungeon;

import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;

public class DungeonInstance
{

	/** ID of the Dungeon. */
	public final int id;
	/** RNG for floor generation. */
	public final Random random;

	public DungeonInstance(int id, Random random)
	{
		this.id = id;
		this.random = random;
	}

	public Floor createFloor(int floorID)
	{
		return new Floor(floorID, this.dungeon().getLayout(floorID), this, new Random(this.random.nextLong()));
	}

	public Dungeon dungeon()
	{
		return DungeonRegistry.find(this.id);
	}

}

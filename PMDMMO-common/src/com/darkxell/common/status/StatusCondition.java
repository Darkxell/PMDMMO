package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;

public class StatusCondition
{

	/** This Status Condition's ID. */
	public final int id;

	public StatusCondition(int id)
	{
		this.id = id;
	}

	public ArrayList<DungeonEvent> tick(Floor floor, StatusConditionInstance instance)
	{
		return new ArrayList<DungeonEvent>();
	}

}

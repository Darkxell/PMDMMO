package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;

public class ExplorationStopEvent extends DungeonEvent
{

	public ExplorationStopEvent(Floor floor)
	{
		super(floor);
	}

	@Override
	public String loggerMessage()
	{
		return "Dungeon exploration stopped.";
	}

}

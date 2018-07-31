package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;

public class BossDefeatedEvent extends DungeonEvent
{

	public BossDefeatedEvent(Floor floor)
	{
		super(floor);
	}

	@Override
	public String loggerMessage()
	{
		return "The boss has been defeated.";
	}

}

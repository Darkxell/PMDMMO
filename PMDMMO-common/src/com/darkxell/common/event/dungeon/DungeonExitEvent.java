package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.player.Player;

public class DungeonExitEvent extends NextFloorEvent
{

	public DungeonExitEvent(Floor floor)
	{
		super(floor);
	}

	public DungeonExitEvent(Floor floor, Player player)
	{
		super(floor, player);
	}

	@Override
	public String loggerMessage()
	{
		return this.player.name() + " exited the Dungeon.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		return this.resultingEvents;
	}

}

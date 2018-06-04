package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.player.Player;

public class NextFloorEvent extends DungeonEvent
{

	public final Player player;

	public NextFloorEvent(Floor floor, Player player)
	{
		super(floor);
		this.player = player;
	}

	@Override
	public String loggerMessage()
	{
		return this.player.name() + " went to the next floor.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.floor.dungeon.endFloor();
		this.floor.onFloorStart(this.resultingEvents);
		return super.processServer();
	}

}

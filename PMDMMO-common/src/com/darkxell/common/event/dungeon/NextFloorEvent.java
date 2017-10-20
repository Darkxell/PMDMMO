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
	public ArrayList<DungeonEvent> processServer()
	{
		this.floor.dungeon.endFloor();
		this.resultingEvents.addAll(this.floor.dungeon.currentFloor().onFloorStart());
		return super.processServer();
	}

}

package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.DungeonOutcome.Outcome;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.player.Player;

public class DungeonExitEvent extends DungeonEvent
{

	public final Player player;

	public DungeonExitEvent(Floor floor, Player player)
	{
		super(floor);
		this.player = player;
	}

	@Override
	public String loggerMessage()
	{
		return this.player.name() + " exited the Dungeon.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		DungeonOutcome outcome = new DungeonOutcome(Outcome.DUNGEON_CLEARED, this.floor.dungeon.id);
		this.resultingEvents.add(new ExplorationStopEvent(this.floor, outcome));
		return this.resultingEvents;
	}

}

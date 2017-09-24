package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;

public class GameTurn
{

	/** Lists the Events that occur in this turn. */
	private ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
	/** The Floor this Turn occurs in. */
	public final Floor floor;
	private boolean turnEnded = false;

	public GameTurn(Floor floor)
	{
		this.floor = floor;
	}

	public void addEvent(DungeonEvent event)
	{
		this.events.add(event);
	}

	/** @return The list of Events that happened during this Turn. */
	public DungeonEvent[] events()
	{
		return this.events.toArray(new DungeonEvent[this.events.size()]);
	}

	public ArrayList<DungeonEvent> onTurnEnd()
	{
		this.turnEnded = true;
		return new ArrayList<DungeonEvent>();
	}

	/** @return True if the End turn Events have already been checked. */
	public boolean turnEnded()
	{
		return this.turnEnded;
	}

}

package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;

public class GameTurn
{
	
	public static final int SUB_TURNS = 4;

	/** Lists the Events that occur in this turn. */
	private ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
	/** The Floor this Turn occurs in. */
	public final Floor floor;
	public boolean turnEnded = false;

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

}

package com.darkxell.common.event.turns;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class GameTurn
{
	
	public static final int SUB_TURNS = Stat.MAX_SPEED;

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

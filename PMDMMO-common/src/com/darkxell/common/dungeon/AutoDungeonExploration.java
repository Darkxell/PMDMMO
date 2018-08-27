package com.darkxell.common.dungeon;

import java.util.LinkedList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.EventCommunication;
import com.eclipsesource.json.JsonObject;

/** DungeonInstance that reads DungeonEvents stored as Json Objects and executes them.<br>
 * Used to check an exploration for client cheats. */
public class AutoDungeonExploration extends DungeonExploration
{

	public LinkedList<JsonObject> pendingEvents = new LinkedList<>();

	public AutoDungeonExploration(int id, long seed)
	{
		super(id, seed);
	}

	public DungeonEvent getNextEvent()
	{
		return this.nextEvent(false);
	}

	@Override
	public Floor initiateExploration()
	{
		Floor floor = super.initiateExploration();
		this.eventProcessor.processPending();
		return floor;
	}

	public DungeonEvent nextEvent()
	{
		return this.nextEvent(true);
	}

	private DungeonEvent nextEvent(boolean checkOnly)
	{
		DungeonEvent e = null;
		do
		{
			if (this.pendingEvents.isEmpty()) return null;
			e = EventCommunication.read(checkOnly ? this.pendingEvents.peekFirst() : this.pendingEvents.removeFirst(), this.currentFloor());
		} while (e == null);
		e.setPAE();
		return e;
	}

}

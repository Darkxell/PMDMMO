package com.darkxell.common.dungeon;

import java.util.LinkedList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.EventCommunication;
import com.eclipsesource.json.JsonObject;

public class AutoDungeonInstance extends DungeonInstance
{

	public LinkedList<JsonObject> pendingEvents = new LinkedList<>();

	public AutoDungeonInstance(int id, long seed)
	{
		super(id, seed);
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
		DungeonEvent e = null;
		do
		{
			if (this.pendingEvents.isEmpty()) return null;
			e = EventCommunication.read(this.pendingEvents.removeFirst(), this.currentFloor());
		} while (e == null);
		e.setPAE();
		return e;
	}

}

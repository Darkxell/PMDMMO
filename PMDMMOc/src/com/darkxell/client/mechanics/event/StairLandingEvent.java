package com.darkxell.client.mechanics.event;

import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.common.event.DungeonEvent;

public class StairLandingEvent extends DungeonEvent
{

	public StairLandingEvent()
	{
		super(DungeonPersistance.floor);
	}

}

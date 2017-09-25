package com.darkxell.client.mechanics.event;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.common.event.DungeonEvent;

public class StairLandingEvent extends DungeonEvent {

	public StairLandingEvent() {
		super(Persistance.floor);
	}

}

package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.weather.WeatherInstance;

public class WeatherChangedEvent extends DungeonEvent
{

	public final WeatherInstance previous, next;

	public WeatherChangedEvent(Floor floor, WeatherInstance previous, WeatherInstance next)
	{
		super(floor);
		this.previous = previous;
		this.next = next;
	}

}

package com.darkxell.common.event.dungeon;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.weather.WeatherInstance;

public class WeatherCleanedEvent extends DungeonEvent
{

	public final WeatherInstance weather;

	public WeatherCleanedEvent(WeatherInstance weather)
	{
		super(weather.floor);
		this.weather = weather;
	}

	// TODO process Server

}

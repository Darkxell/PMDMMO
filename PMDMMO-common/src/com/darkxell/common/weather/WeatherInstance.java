package com.darkxell.common.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.WeatherCleanedEvent;

public class WeatherInstance
{

	public final int duration;
	public final Floor floor;
	public final WeatherSource source;
	private int ticksLeft;
	public final Weather weather;

	public WeatherInstance(Weather weather, WeatherSource source, Floor floor, int duration)
	{
		this.weather = weather;
		this.source = source;
		this.floor = floor;
		this.ticksLeft = this.duration = duration;
	}

	public ArrayList<DungeonEvent> update()
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		boolean isOver = false;

		if (this.ticksLeft != -1)
		{
			--this.ticksLeft;
			if (this.ticksLeft == 0)
			{
				isOver = true;
				events.add(new WeatherCleanedEvent(this));
			}
		}

		if (!isOver) events.addAll(this.weather.weatherTick(this.floor));

		return events;
	}

}

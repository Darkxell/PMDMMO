package com.darkxell.common.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.weather.WeatherCleanedEvent;

public class ActiveWeather implements Comparable<ActiveWeather>
{

	public static final int DEFAULT_DURATION = 20;

	/** -1 for infinite duration. */
	public final int duration;
	public final Floor floor;
	public final int priority;
	/** May be null for the Floor's prevailing weather. */
	public final WeatherSource source;
	protected int tick;
	public final Weather weather;

	public ActiveWeather(Weather weather, WeatherSource source, int priority, Floor floor, int duration)
	{
		this.weather = weather;
		this.source = source;
		this.priority = priority;
		this.floor = floor;
		this.tick = 0;
		this.duration = duration;
	}

	@Override
	public int compareTo(ActiveWeather o)
	{
		return -Integer.compare(this.priority, o.priority);
	}

	protected boolean isOver()
	{
		if (this.duration == -1) return this.source != null && this.source.isOver();
		return this.tick >= this.duration;
	}

	public void update(ArrayList<DungeonEvent> events)
	{
		boolean isOver = false;

		++this.tick;

		if (this.isOver())
		{
			isOver = true;
			events.add(new WeatherCleanedEvent(this));
		}

		if (!isOver) events.addAll(this.weather.weatherTick(this.floor, this.tick));
	}

}

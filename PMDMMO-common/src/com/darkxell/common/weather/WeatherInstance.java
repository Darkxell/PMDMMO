package com.darkxell.common.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.WeatherCleanedEvent;

public class WeatherInstance implements Comparable<WeatherInstance>
{

	/** -1 for infinite duration. */
	public final int duration;
	public final Floor floor;
	public final int priority;
	/** May be null for the Floor's prevailing weather. */
	public final WeatherSource source;
	protected int ticksLeft;
	public final Weather weather;

	public WeatherInstance(Weather weather, WeatherSource source, int priority, Floor floor, int duration)
	{
		this.weather = weather;
		this.source = source;
		this.priority = priority;
		this.floor = floor;
		this.ticksLeft = this.duration = duration;
	}

	@Override
	public int compareTo(WeatherInstance o)
	{
		return -Integer.compare(this.priority, o.priority);
	}

	protected boolean isOver()
	{
		return this.ticksLeft == 0;
	}

	public ArrayList<DungeonEvent> update()
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		boolean isOver = false;

		if (this.ticksLeft != -1) --this.ticksLeft;

		if (this.isOver())
		{
			isOver = true;
			events.add(new WeatherCleanedEvent(this));
		}

		if (!isOver) events.addAll(this.weather.weatherTick(this.floor));

		return events;
	}

}

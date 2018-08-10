package com.darkxell.common.event.dungeon.weather;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.ActiveWeather;

public class WeatherChangedEvent extends DungeonEvent
{

	/** The new current Weather. */
	public final ActiveWeather next;
	/** The previous Weather. may be null if this describes the application of the Floor's prevailing Weather. */
	public final ActiveWeather previous;

	public WeatherChangedEvent(Floor floor, ActiveWeather previous, ActiveWeather next)
	{
		super(floor);
		this.previous = previous;
		this.next = next;
	}

	@Override
	public String loggerMessage()
	{
		this.messages.add(new Message("weather.changed").addReplacement("<weather>", this.next.weather.name()));
		return this.next.weather.name() + " is now the main Weather.";
	}

}

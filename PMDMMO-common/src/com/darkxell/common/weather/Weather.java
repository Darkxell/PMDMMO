package com.darkxell.common.weather;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Animation;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;

// TODO make abstract
public class Weather
{

	public static final byte clear = 0, snow = 1, hail = 2, sunny = 3, sandstorm = 4, fog = 5, clouds = 6, rain = 7, random = -1;

	public static final Weather CLEAR = new Weather(clear);
	public static final Weather CLOUDS = new Weather(clouds);
	public static final Weather FOG = new Weather(fog);
	public static final Weather HAIL = new Weather(hail);
	public static final Weather RAIN = new Weather(rain);
	public static final Weather SANDSTORM = new Weather(sandstorm);
	public static final Weather SNOW = new Weather(snow);
	public static final Weather SUNNY = new Weather(sunny);

	private static final HashMap<Integer, Weather> weatherRegistry = new HashMap<Integer, Weather>();

	public static Weather find(int id)
	{
		return weatherRegistry.get(id);
	}

	public final int id;

	private Weather(int id)
	{
		this.id = id;
		weatherRegistry.put(this.id, this);
	}

	/** @return The Animation to play when this Weather is set as current. */
	public Animation animation()
	{
		return null;
	}

	/** Called whenever this Weather ticks. */
	public ArrayList<DungeonEvent> weatherTick(Floor floor)
	{
		return new ArrayList<DungeonEvent>();
	}

}

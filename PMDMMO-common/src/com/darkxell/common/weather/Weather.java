package com.darkxell.common.weather;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Animation;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.util.language.Message;

// TODO make abstract
public class Weather
{

	private static final HashMap<Integer, Weather> _weatherRegistry = new HashMap<Integer, Weather>();

	public static final byte clear = 0, snow = 1, hail = 2, sunny = 3, sandstorm = 4, fog = 5, clouds = 6, rain = 7, random = -1;

	public static final Weather CLEAR = new Weather(clear, null);
	public static final Weather CLOUDS = new Weather(clouds, new Color(200, 200, 200, 100));
	public static final Weather FOG = new Weather(fog, new Color(150, 150, 150, 100));
	public static final Weather HAIL = new Weather(hail, new Color(150, 150, 200, 100));
	public static final Weather RAIN = new Weather(rain, new Color(100, 100, 150, 100));
	public static final Weather SANDSTORM = new Weather(sandstorm, new Color(100, 150, 150, 100));
	public static final Weather SNOW = new Weather(snow, new Color(250, 250, 250, 100));
	public static final Weather SUNNY = new Weather(sunny, new Color(150, 200, 200, 100));

	public static Weather find(int id)
	{
		if (!_weatherRegistry.containsKey(id)) return CLEAR;
		return _weatherRegistry.get(id);
	}

	public final int id;
	/** The Color to fill the screen with as a Weather Layer. May be null if no Layer. */
	public final Color layer;

	private Weather(int id, Color layer)
	{
		this.id = id;
		this.layer = layer;
		_weatherRegistry.put(this.id, this);
	}

	/** @return The Animation to play when this Weather is set as current. May return null if no Animation. */
	public Animation animation()
	{
		return null;
	}

	public Message name()
	{
		return new Message("weather." + this.id);
	}

	/** Called whenever this Weather ticks. */
	public ArrayList<DungeonEvent> weatherTick(Floor floor)
	{
		return new ArrayList<DungeonEvent>();
	}

}

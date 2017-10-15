package com.darkxell.common.weather;

import java.util.Comparator;

public interface WeatherSource
{

	public static final Comparator<WeatherSource> comparator = new Comparator<WeatherSource>()
	{
		@Override
		public int compare(WeatherSource o1, WeatherSource o2)
		{
			return -Integer.compare(o1.priority(), o2.priority());
		}
	};

	public int priority();

}

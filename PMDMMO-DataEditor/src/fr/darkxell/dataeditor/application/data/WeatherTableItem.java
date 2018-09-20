package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.weather.Weather;

public class WeatherTableItem
{

	private FloorSet floors;
	private Integer weather;

	public WeatherTableItem(Integer weather, FloorSet floors)
	{
		this.weather = weather;
		this.floors = floors;
	}

	public FloorSet getFloors()
	{
		return this.floors;
	}

	public Weather getWeather()
	{
		return Weather.find(this.weather);
	}

}

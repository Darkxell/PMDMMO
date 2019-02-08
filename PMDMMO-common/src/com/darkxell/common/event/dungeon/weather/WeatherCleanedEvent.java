package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.weather.ActiveWeather;

public class WeatherCleanedEvent extends DungeonEvent {

    public final ActiveWeather weather;

    public WeatherCleanedEvent(ActiveWeather weather) {
        super(weather.floor);
        this.weather = weather;
    }

    @Override
    public String loggerMessage() {
        return this.weather.weather.name() + " ended.";
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.floor.removeWeather(this.weather, this.resultingEvents);
        return super.processServer();
    }

}

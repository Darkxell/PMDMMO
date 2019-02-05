package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.weather.ActiveWeather;

public class WeatherCreatedEvent extends DungeonEvent {

    public final ActiveWeather weather;

    public WeatherCreatedEvent(ActiveWeather weather) {
        super(weather.floor);
        this.weather = weather;
    }

    @Override
    public String loggerMessage() {
        return this.weather.weather.name() + " started.";
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.floor.setWeather(this.weather, this.resultingEvents);
        return super.processServer();
    }

}

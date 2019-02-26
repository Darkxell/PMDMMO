package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.weather.ActiveWeather;

public class WeatherCleanedEvent extends Event {

    public final ActiveWeather weather;

    public WeatherCleanedEvent(ActiveWeather weather, EventSource eventSource) {
        super(weather.floor, eventSource);
        this.weather = weather;
    }

    @Override
    public String loggerMessage() {
        return this.weather.weather.name() + " ended.";
    }

    @Override
    public ArrayList<Event> processServer() {
        this.floor.removeWeather(this, this.resultingEvents);
        return super.processServer();
    }

}

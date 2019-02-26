package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.weather.ActiveWeather;

public class WeatherCreatedEvent extends Event {

    public final ActiveWeather weather;

    public WeatherCreatedEvent(ActiveWeather weather, EventSource eventSource) {
        super(weather.floor, eventSource);
        this.weather = weather;
    }

    @Override
    public String loggerMessage() {
        return this.weather.weather.name() + " started.";
    }

    @Override
    public ArrayList<Event> processServer() {
        this.floor.setWeather(this, this.resultingEvents);
        return super.processServer();
    }

}

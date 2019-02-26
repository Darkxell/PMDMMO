package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.weather.ActiveWeather;

public class PersistantWeatherChangedEvent extends Event {

    public final ActiveWeather weather;

    public PersistantWeatherChangedEvent(Floor floor, DungeonEventSource eventSource, ActiveWeather weather) {
        super(floor, eventSource);
        this.weather = weather;
    }

    @Override
    public String loggerMessage() {
        return "Persistant weather changed to " + this.weather.weather.name();
    }

    @Override
    public ArrayList<Event> processServer() {
        this.floor.setPersistantWeather(this, this.resultingEvents);
        return super.processServer();
    }

}

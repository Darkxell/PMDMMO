package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.weather.ActiveWeather;

public class PersistantWeatherChangedEvent extends DungeonEvent {

    public final ActiveWeather weather;

    public PersistantWeatherChangedEvent(Floor floor, ActiveWeather weather) {
        super(floor, eventSource);
        this.weather = weather;
    }

    @Override
    public String loggerMessage() {
        return "Persistant weather changed to " + this.weather.weather.name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.floor.setPersistantWeather(this.weather, this.resultingEvents);
        return super.processServer();
    }

}

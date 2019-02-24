package com.darkxell.common.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.dungeon.weather.WeatherCleanedEvent;

public class ActiveWeather {

    public static final int DEFAULT_DURATION = 20;

    /** -1 for infinite duration. */
    public final int duration;
    public final Floor floor;
    /** May be null for the Floor's prevailing weather. */
    public final WeatherSource source;
    public int tick;
    public final Weather weather;

    public ActiveWeather(Weather weather, WeatherSource source, Floor floor, int duration) {
        this.weather = weather;
        this.source = source;
        this.floor = floor;
        this.tick = 0;
        this.duration = duration;
    }

    protected boolean isOver() {
        if (this.duration == -1)
            return this.source != null && this.source.isOver();
        return this.tick >= this.duration;
    }

    public void update(ArrayList<DungeonEvent> events) {
        boolean isOver = false;

        ++this.tick;

        if (this.isOver()) {
            isOver = true;
            events.add(new WeatherCleanedEvent(this, DungeonEventSource.TRIGGER));
        }

        if (!isOver)
            events.addAll(this.weather.weatherTick(this.floor, this.tick));
    }

}

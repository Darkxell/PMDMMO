package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.pokemon.Pokemon;

public class LevelupEvent extends Event {

    public final Pokemon pokemon;

    public LevelupEvent(Floor floor, EventSource eventSource, Pokemon pokemon) {
        super(floor, eventSource);
        this.pokemon = pokemon;
    }

    @Override
    public String loggerMessage() {
        return null;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.pokemon.levelUp(this, this.resultingEvents);
        return super.processServer();
    }

}

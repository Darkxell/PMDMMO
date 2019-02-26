package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.Pokemon;

public class LevelupEvent extends Event {

    public final Pokemon pokemon;

    public LevelupEvent(Floor floor, DungeonEventSource eventSource, Pokemon pokemon) {
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

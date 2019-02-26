package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.data.DungeonEncounter.CreatedEncounter;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;

public class PokemonSpawnedEvent extends Event {

    /** The spawned Pokemon. */
    public final CreatedEncounter encounter;

    public PokemonSpawnedEvent(Floor floor, EventSource eventSource, CreatedEncounter encounter) {
        super(floor, eventSource);
        this.encounter = encounter;
    }

    @Override
    public String loggerMessage() {
        return this.encounter.pokemon + " spawned at " + this.encounter.tile;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.floor.summonPokemon(this.encounter.pokemon, this.encounter.tile.x, this.encounter.tile.y,
                this.resultingEvents, this.encounter.ai);
        return super.processServer();
    }

}

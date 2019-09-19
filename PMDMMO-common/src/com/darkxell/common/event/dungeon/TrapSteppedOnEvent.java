package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.language.Message;

public class TrapSteppedOnEvent extends Event {

    public final DungeonPokemon pokemon;
    public final Tile tile;

    public TrapSteppedOnEvent(Floor floor, EventSource eventSource, DungeonPokemon pokemon, Tile tile) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.tile = tile;
    }

    @Override
    public boolean isValid() {
        return this.tile.trap != null && this.pokemon.tile() == this.tile;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " stepped on a " + this.trap().name();
    }

    @Override
    public ArrayList<Event> processServer() {
        this.tile.trapRevealed = true;
        this.messages.add(new Message("trap.stepped").addReplacement("<pokemon>", pokemon.getNickname())
                .addReplacement("<trap>", this.trap().name()));
        this.trap().onPokemonStep(this, this.resultingEvents);
        return super.processServer();
    }

    public Trap trap() {
        return this.tile.trap;
    }

}

package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class PokemonTeleportedEvent extends Event {

    public final Tile destination;
    public final DungeonPokemon pokemon;

    public PokemonTeleportedEvent(Floor floor, EventSource eventSource, DungeonPokemon pokemon, Tile destination) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.destination = destination;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " was teleported to " + this.destination;
    }

    @Override
    public boolean isValid() {
        return this.destination.getPokemon() == null;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.pokemon.tile().removePokemon(this.pokemon);
        this.destination.setPokemon(this.pokemon);
        this.messages.add(new Message("pokemon.teleported").addReplacement("<pokemon>", this.pokemon.getNickname()));
        return super.processServer();
    }

}

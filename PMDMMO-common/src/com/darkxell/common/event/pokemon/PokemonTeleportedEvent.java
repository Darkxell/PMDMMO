package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class PokemonTeleportedEvent extends DungeonEvent {

    public final Tile destination;
    public final DungeonPokemon pokemon;

    public PokemonTeleportedEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon pokemon,
            Tile destination) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.destination = destination;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " was teleported to " + this.destination;
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        if (this.destination.getPokemon() == null) {
            this.pokemon.tile().removePokemon(this.pokemon);
            this.destination.setPokemon(this.pokemon);
            this.messages
                    .add(new Message("pokemon.teleported").addReplacement("<pokemon>", this.pokemon.getNickname()));
        } else
            this.messages.add(new Message("ERROR: Pokemon was teleported to a non-empty Tile!!", false));
        return super.processServer();
    }

}

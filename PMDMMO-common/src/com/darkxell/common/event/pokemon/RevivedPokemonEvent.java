package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class RevivedPokemonEvent extends Event {

    public final DungeonPokemon pokemon;

    public RevivedPokemonEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon pokemon) {
        super(floor, eventSource);
        this.pokemon = pokemon;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " was revived.";
    }

    @Override
    public ArrayList<Event> processServer() {
        this.messages.add(new Message("pokemon.revived").addReplacement("<pokemon>", this.pokemon.getNickname()));
        this.pokemon.revive();
        return super.processServer();
    }

}

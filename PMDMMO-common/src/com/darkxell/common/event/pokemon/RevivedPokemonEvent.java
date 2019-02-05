package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class RevivedPokemonEvent extends DungeonEvent {

    public final DungeonPokemon pokemon;

    public RevivedPokemonEvent(Floor floor, DungeonPokemon pokemon) {
        super(floor);
        this.pokemon = pokemon;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " was revived.";
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.messages.add(new Message("pokemon.revived").addReplacement("<pokemon>", this.pokemon.getNickname()));
        this.pokemon.revive();
        return super.processServer();
    }

}

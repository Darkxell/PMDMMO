package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.language.Message;

public class TrapSteppedOnEvent extends DungeonEvent {

    public final DungeonPokemon pokemon;
    public final Tile tile;
    public final Trap trap;

    public TrapSteppedOnEvent(Floor floor, DungeonPokemon pokemon, Tile tile, Trap trap) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.tile = tile;
        this.trap = trap;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " stepped on a " + this.trap.name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.tile.trapRevealed = true;
        this.messages.add(new Message("trap.stepped").addReplacement("<pokemon>", pokemon.getNickname())
                .addReplacement("<trap>", this.trap.name()));
        this.trap.onPokemonStep(this.floor, this.pokemon, this.resultingEvents);
        return super.processServer();
    }

}

package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.Pokemon;

public class MoveDiscoveredEvent extends DungeonEvent {

    public final Move move;
    public final Pokemon pokemon;

    public MoveDiscoveredEvent(Floor floor, Pokemon pokemon, Move move) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.move = move;
    }

    @Override
    public String loggerMessage() {
        return null;
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        if (this.pokemon.moveCount() < 4)
            this.resultingEvents
                    .add(new MoveLearnedEvent(this.floor, this.pokemon, this.move, this.pokemon.moveCount()));
        return super.processServer();
    }

}

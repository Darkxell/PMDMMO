package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class MoveLearnedEvent extends DungeonEvent {

    public final int index;
    public final Move move;
    public final Pokemon pokemon;

    public MoveLearnedEvent(Floor floor, Pokemon pokemon, Move move, int index) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.move = move;
        this.index = index;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " learned " + this.move;
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.messages.add(new Message("moves.learned").addReplacement("<pokemon>", this.pokemon.getNickname())
                .addReplacement("<move>", this.move.name()));

        LearnedMove move = new LearnedMove(this.move.id);
        this.pokemon.setMove(this.index, move);
        this.floor.dungeon.communication.moveIDs.register(move, this.pokemon);
        return super.processServer();
    }

}

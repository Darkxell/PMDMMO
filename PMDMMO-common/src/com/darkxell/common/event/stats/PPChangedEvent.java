package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class PPChangedEvent extends DungeonEvent {

    public static final int CHANGE_ALL_MOVES = 10;

    /** The move to set the PP of. If 10, changes all moves. */
    public final int moveIndex;
    public final DungeonPokemon pokemon;
    public final int pp;

    public PPChangedEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon pokemon, int pp, int moveIndex) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.pp = pp;
        this.moveIndex = moveIndex;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " had its PP changed by " + this.pp;
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        String mid = this.pp >= 0 ? "moves.pp_restored" : "moves.pp_lost";
        if (this.moveIndex != CHANGE_ALL_MOVES)
            mid = this.pp >= 0 ? "moves.pp_restored_one" : "moves.pp_lost_one";
        Message me = new Message(mid).addReplacement("<pokemon>", this.pokemon.getNickname());
        if (this.moveIndex < this.pokemon.moveCount())
            me.addReplacement("<move>", this.pokemon.move(this.moveIndex).move().name());
        this.messages.add(me);

        if (this.moveIndex == CHANGE_ALL_MOVES)
            for (int m = 0; m < this.pokemon.moveCount(); ++m)
                this.pokemon.move(m).setPP(this.pokemon.move(m).pp() + this.pp);
        else if (this.moveIndex < this.pokemon.moveCount())
            this.pokemon.move(this.moveIndex).setPP(this.pokemon.move(this.moveIndex).pp() + this.pp);
        return super.processServer();
    }

}

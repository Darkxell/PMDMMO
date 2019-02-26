package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class IncreasedIQEvent extends Event {

    public final int iq;
    public final DungeonPokemon pokemon;

    public IncreasedIQEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon pokemon, int iq) {
        super(floor, eventSource);
        this.iq = iq;
        this.pokemon = pokemon;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + "'s IQ increased by " + this.iq;
    }

    @Override
    public ArrayList<Event> processServer() {
        int satisfaction;
        if (this.iq >= 9)
            satisfaction = 3;
        else if (this.iq >= 5)
            satisfaction = 2;
        else if (this.iq >= 3)
            satisfaction = 1;
        else
            satisfaction = 0;
        this.messages.add(new Message("iq." + satisfaction));

        this.pokemon.increaseIQ(this.iq);
        return super.processServer();
    }

}

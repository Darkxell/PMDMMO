package com.darkxell.client.mechanics.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.action.PokemonTravelEvent;

public class PokemonTravelsEvent extends Event {
    private PokemonTravelEvent[] events;

    public PokemonTravelsEvent(Floor floor, ArrayList<PokemonTravelEvent> travels) {
        super(floor, DungeonEventSource.CLIENT_PURPUSES);
        this.events = travels.toArray(new PokemonTravelEvent[0]);
    }

    @Override
    public String loggerMessage() {
        String s = "";
        boolean first = true;
        for (PokemonTravelEvent e : this.events) {
            if (first) s += ", ";
            s += e.pokemon();
        }
        return s + " travel.";
    }

    @Override
    public ArrayList<Event> processServer() {
        for (PokemonTravelEvent e : this.events)
            this.resultingEvents.addAll(e.processServer());
        return super.processServer();
    }

    public PokemonTravelEvent[] travels() {
        return this.events.clone();
    }

}

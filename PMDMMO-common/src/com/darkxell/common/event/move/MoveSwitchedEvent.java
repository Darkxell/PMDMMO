package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class MoveSwitchedEvent extends Event implements Communicable {

    /** The indices of the switched moves. */
    private int from, to;
    /** The Pokemon whose moves are switched. */
    private Pokemon pokemon;

    public MoveSwitchedEvent(Floor floor, DungeonEventSource eventSource) {
        super(floor, eventSource);
    }

    public MoveSwitchedEvent(Floor floor, DungeonEventSource eventSource, Pokemon pokemon, int from, int to) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MoveSwitchedEvent))
            return false;
        MoveSwitchedEvent o = (MoveSwitchedEvent) obj;
        return this.pokemon.id() == o.pokemon.id() && this.from == o.from && this.to == o.to;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + "switched moves " + this.from + " and " + this.to;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.pokemon.switchMoves(this.from, this.to);
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        try {
            this.pokemon = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("pokemon", 0));
            if (this.pokemon == null)
                throw new JsonReadingException("No pokemon with ID " + value.getLong("pokemon", 0));
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Pokemon ID: " + value.get("pokemon"));
        }
        try {
            this.from = value.getInt("from", 0);
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for from: " + value.get("from"));
        }
        try {
            this.to = value.getInt("to", 0);
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for to: " + value.get("to"));
        }
        if (this.from == this.to)
            throw new JsonReadingException("From and to can't be the same indices.");
    }

    @Override
    public JsonObject toJson() {
        return Json.object().add("pokemon", this.pokemon.id()).add("from", this.from).add("to", this.to);
    }

}

package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Direction;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class PokemonRotateEvent extends DungeonEvent implements Communicable {

    private Direction direction;
    private DungeonPokemon pokemon;

    public PokemonRotateEvent(Floor floor, DungeonEventSource eventSource) {
        super(floor, eventSource);
    }

    /**
     * @param eventSource TODO
     * @param pokemon   - The Pokemon that rotates.
     * @param direction - The new direction the Pokemon should face.
     */
    public PokemonRotateEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon pokemon, Direction direction) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PokemonRotateEvent))
            return false;
        PokemonRotateEvent o = (PokemonRotateEvent) obj;
        return this.direction == o.direction && this.pokemon.id() == o.pokemon.id();
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + " rotated to face " + this.direction.lowercaseName();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.pokemon.setFacing(this.direction);
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        try {
            Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("pokemon", 0));
            if (p == null)
                throw new JsonReadingException("No pokemon with ID " + value.getLong("pokemon", 0));
            this.pokemon = p.getDungeonPokemon();
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Pokemon ID: " + value.get("pokemon"));
        }

        try {
            this.direction = Direction.valueOf(value.getString("direction", Direction.NORTH.name()));
        } catch (IllegalArgumentException e) {
            throw new JsonReadingException("No direction with name " + value.getString("direction", "null"));
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("pokemon", this.pokemon.id());
        root.add("direction", this.direction.name());
        return root;
    }

}

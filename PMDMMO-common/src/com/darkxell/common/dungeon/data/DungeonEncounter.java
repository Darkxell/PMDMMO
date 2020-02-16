package com.darkxell.common.dungeon.data;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.ai.StationaryWildAI;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.model.dungeon.DungeonEncounterModel;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;

/** Describes how a Pokemon appears in a Dungeon. */
public class DungeonEncounter {

    /** Utility class created when spawning a Pokemon. */
    public static class CreatedEncounter {

        /** May be null. If so, AI will be created automatically when calling {@link Floor#summonPokemon}. */
        public AI ai;
        public DungeonPokemon pokemon;
        public Tile tile;

        public CreatedEncounter(DungeonPokemon pokemon, Tile tile, AI ai) {
            this.pokemon = pokemon;
            this.tile = tile;
            this.ai = ai;
        }

    }

    private final DungeonEncounterModel model;

    public DungeonEncounter(int id, int level, int weight, FloorSet floors, CustomAI aiType) {
        this(new DungeonEncounterModel(id, level, weight, aiType, floors));
    }

    public DungeonEncounter(DungeonEncounterModel model) {
        this.model = model;
    }

    public CreatedEncounter generate(Floor floor) {
        DungeonPokemon pokemon = new DungeonPokemon(this.pokemon().generate(floor.random, this.getLevel()));
        Tile tile = floor.randomEmptyTile(true, true, floor.random);
        AI ai;
        switch (this.getAIType()) {
        case STATIONARY:
            ai = new StationaryWildAI(floor, pokemon);
            break;

        default:
            ai = null;
            break;
        }
        return new CreatedEncounter(pokemon, tile, ai);
    }

    public CustomAI getAIType() {
        return this.model.getAiType();
    }

    public FloorSet getFloors() {
        return this.model.getFloors();
    }

    public int getID() {
        return this.model.getID();
    }

    public int getLevel() {
        return this.model.getLevel();
    }

    public int getWeight() {
        return this.model.getWeight();
    }

    public PokemonSpecies pokemon() {
        return Registries.species().find(this.getID());
    }

    public DungeonEncounterModel getModel() {
        return this.model;
    }

}

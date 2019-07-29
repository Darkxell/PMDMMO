package com.darkxell.client.resources.images;

import java.util.ArrayList;

import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesets;
import com.darkxell.client.resources.image.tileset.dungeon.AbstractFloorDungeonTileset;
import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.pokemon.PokemonRegistry;

public final class SpriteLoader {

    /**
     * Variable to temporarily store IDs of sprites to load. Using ArrayList and not HashSet because some Sprites are
     * more likely to be used first than others.
     */
    private static final ArrayList<String> toLoad = new ArrayList<>();

    /**
     * Called when the user is about to enter a Dungeon.<br>
     * This method searches the entire Dungeon for Sprites that may be used in it and preloads those sprites.
     */
    public static void loadDungeon(DungeonExploration dungeon) {
        for (int floor = 0; floor < dungeon.dungeon().floorCount; ++floor)
            loadFloor(dungeon.dungeon(), floor);

        toLoad.clear();
    }

    private static void loadFloor(Dungeon dungeon, int floor) {
        add(AbstractFloorDungeonTileset.getTilesetPath(dungeon, floor));
        PokemonRegistry species = Registries.species();
        for (DungeonEncounter e : dungeon.pokemon(floor)) {
            PokemonSpritesets.loadSpriteset(species.find(e.id), false);
            PokemonSpritesets.loadSpriteset(species.find(e.id), true);
        }
    }

    private static void add(String path) {
        if (!toLoad.contains(path))
            toLoad.add(path);
    }

    private SpriteLoader() {
    }

}

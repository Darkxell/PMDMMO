package com.darkxell.common.registry;

import java.io.IOException;
import java.net.URL;

import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.trap.TrapRegistry;

public class Registries {
    private static Registries instance;

    public static Registries instance() {
        return instance;
    }

    public static void load() {
        try {
            instance = new Registries();
        } catch (IOException e) {
            throw new AssertionError("Could not load registries.", e);
        }
    }

    private static URL getResource(String path) {
        return Registries.class.getResource(path);
    }

    public static PokemonRegistry species() {
        return instance.species;
    }

    public static DungeonRegistry dungeons() {
        return instance.dungeons;
    }

    public static ItemRegistry items() {
        return instance.items;
    }

    public static MoveRegistry moves() {
        return instance.moves;
    }

    public static TrapRegistry traps() {
        return instance.traps;
    }

    public static void save() throws IOException {
        instance.species.save();
        instance.dungeons.save();
        instance.items.save();
        instance.moves.save();
        instance.traps.save();
    }

    private PokemonRegistry species;
    private DungeonRegistry dungeons;
    private ItemRegistry items;
    private MoveRegistry moves;
    private TrapRegistry traps;

    private Registries() throws IOException {
        this.species = new PokemonRegistry(getResource("/data/pokemon.xml"));
        this.dungeons = new DungeonRegistry(getResource("/data/dungeons.xml"));
        this.items = new ItemRegistry(getResource("/data/items.xml"));
        this.moves = new MoveRegistry(getResource("/data/moves.xml"));
        this.traps = new TrapRegistry();
    }
}

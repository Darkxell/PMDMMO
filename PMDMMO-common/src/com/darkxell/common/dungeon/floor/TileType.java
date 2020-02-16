package com.darkxell.common.dungeon.floor;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;

public enum TileType {

    AIR(5, 'a'),
    GROUND(0, ' '),
    LAVA(4, 'l'),
    STAIR(6, 'S'),
    WALL(1, 'M'),
    WALL_END(2, 'm'),
    WARP_ZONE(7, 'x'),
    WATER(3, 'w');

    public enum Mobility {
        Fire,
        Flying,
        Ghost,
        Normal,
        Water
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<TileType>[] tileGroups = new ArrayList[] { new ArrayList<TileType>(),
            new ArrayList<TileType>() };
    static {
        tileGroups[0].add(WALL);
        tileGroups[0].add(WALL_END);

        tileGroups[1].add(GROUND);
        tileGroups[1].add(STAIR);
        tileGroups[1].add(WARP_ZONE);
    }

    /** @return The Tile type with the input character. */
    public static TileType find(char c) {
        for (TileType type : values())
            if (type.c == c)
                return type;
        return null;
    }

    /** @return The Tile type with the input id. */
    public static TileType find(int id) {
        for (TileType type : values())
            if (type.id == id)
                return type;
        return null;
    }

    /** Character for debug purpuses. */
    public final char c;
    public final int id;

    TileType(int id, char c) {
        this.id = id;
        this.c = c;
    }

    public boolean canWalkOn(DungeonPokemon pokemon) {
        if (pokemon.species().getMobility() == Mobility.Ghost)
            return this != WALL_END;
        if (pokemon.species().getMobility() == Mobility.Water && this == WATER)
            return true;
        if (pokemon.species().getMobility() == Mobility.Fire && this == LAVA)
            return true;
        if (pokemon.species().getMobility() == Mobility.Flying && this == AIR)
            return true;
        return this == GROUND || this == STAIR;
    }

    /** @return True if this Tile connects to the input Tile. */
    public boolean connectsTo(TileType type) {
        if (this == type)
            return true;
        for (ArrayList<TileType> group : tileGroups)
            if (group.contains(this) && group.contains(type))
                return true;
        return false;
    }

}

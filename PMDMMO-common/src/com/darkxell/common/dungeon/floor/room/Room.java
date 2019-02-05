package com.darkxell.common.dungeon.floor.room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.util.xml.XMLUtils;

/** Represents a Room in a Floor. */
public abstract class Room {
    /** The floor of this Room. */
    public final Floor floor;
    /** True if this Room is a Monster House. */
    public final boolean isMonsterHouse;

    public Room(Floor floor, boolean isMonsterHouse) {
        this.floor = floor;
        this.isMonsterHouse = isMonsterHouse;
    }

    public Room(Floor floor, Element xml) {
        this.floor = floor;
        this.isMonsterHouse = XMLUtils.getAttribute(xml, "mhouse", false);
    }

    /** @return True if the input coordinates are inside this Room. */
    public abstract boolean contains(int x, int y);

    public boolean contains(Tile tile) {
        if (tile == null)
            return false;
        return this.contains(tile.x, tile.y);
    }

    /** @return The list of Tiles that allow to leave this Room. */
    public ArrayList<Tile> exits() {
        ArrayList<Tile> exits = new ArrayList<>(this.outline());
        exits.removeIf((Tile t) -> {
            return t.type() != TileType.GROUND;
        });
        return exits;
    }

    /** @return All tiles in this Room. */
    public abstract ArrayList<Tile> listTiles();

    /** @return All tiles that touch this Room. */
    public abstract HashSet<Tile> outline();

    /** @return A random tile in this Room. */
    public Tile randomTile(Random random) {
        ArrayList<Tile> candidates = this.listTiles();
        return candidates.get(random.nextInt(candidates.size()));
    }

    /**
     * @param  empty - <code>true</code> if the tile should have no Pokemon.
     * @return       A random tile in this Room matching the input Tile type.
     */
    public Tile randomTile(Random random, TileType type, boolean empty) {
        ArrayList<Tile> candidates = this.listTiles();
        candidates.removeIf(tile -> tile.type() != type || (empty && tile.getPokemon() != null));
        return candidates.get(random.nextInt(candidates.size()));
    }
}

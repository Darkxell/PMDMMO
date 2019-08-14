package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.RandomUtil;

/** Represents a Floor's layout. */
public abstract class Layout {

    public static final int LAYOUT_STATIC = 0;
    public static final int LAYOUT_SINGLEROOM = 1;
    public static final int LAYOUT_TINYWOODS_2x2 = 2;
    public static final int LAYOUT_THUNDERWAVE_3x3 = 3;
    public static final int LAYOUT_MTSTEELF6_3x2TALL = 4;
    public static final int LAYOUT_MTSTEELF8_4x2TALL = 5;
    public static final int LAYOUT_MTSTEELF7_2x3COLUMNS = 6;
    public static final int LAYOUT_SINISTER_4LINES = 7;
    public static final int LAYOUT_SINISTER_5x5WATER = 8;

    /** @return A new layout from the input ID. */
    public static Layout find(int id) {
        switch (id) {
        case LAYOUT_STATIC:
            return new StaticLayout();
        case LAYOUT_SINGLEROOM:
            return new SingleRoomLayout();
        case LAYOUT_TINYWOODS_2x2:
            return new GridRoomsLayout(id, 2, 2, 5, 5, 14, 14, 2, 5, 5);
        case LAYOUT_THUNDERWAVE_3x3:
            return new GridRoomsLayout(id, 3, 3, 6, 5, 15, 15, 6, 5, 5);
        case LAYOUT_MTSTEELF6_3x2TALL:
            return new GridRoomsLayout(id, 3, 2, 6, 6, 15, 21, 2, 5, 7);
        case LAYOUT_MTSTEELF8_4x2TALL:
            return new GridRoomsLayout(id, 4, 2, 5, 13, 15, 25, 3, 5, 5);
        case LAYOUT_MTSTEELF7_2x3COLUMNS:
            return new GridRoomsLayout(id, 2, 3, 9, 5, 17, 15, 6, 20, 5);
        case LAYOUT_SINISTER_4LINES:
            return new GridRoomsLayout(id, 2, 4, 5, 5, 11, 11, 0, 25, 2);
        case LAYOUT_SINISTER_5x5WATER:
            return new GridRoomsLayout(id, 5, 5, 4, 4, 8, 8, 0, 3, 3, true);
        default:
            Logger.e("Unknown floor type ID : " + id + " (used gridroom2x2 by default)");
            return new GridRoomsLayout(id, 2, 2, 5, 5, 14, 14, 10, 5, 5);
        }
    }

    /** Temporary storage for the generating Floor. */
    protected Floor floor;
    /** RNG */
    protected Random random;

    protected void createDefaultTiles(int width, int height) {
        this.floor.tiles = new Tile[width][height];
        for (int x = 0; x < this.floor.tiles.length; ++x)
            for (int y = 0; y < this.floor.tiles[x].length; ++y)
                if (x == 0 || x == this.floor.tiles.length - 1 || y == 0 || y >= this.floor.tiles[x].length - 1)
                    this.floor.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL_END);
                else
                    this.floor.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL);
    }

    protected void fillRoomsWithGround() {
        for (Room r : this.floor.rooms)
            for (Tile t : r.listTiles())
                t.setType(TileType.GROUND);
    }

    /**
     * Generates a Floor.
     *
     * @param  floor - The Floor to build.
     * @return       The generated Tiles and Rooms.
     */
    public void generate(Floor floor) {
        this.floor = floor;
        this.random = this.floor.random;
        this.generateRooms();
        this.generatePaths();
        this.generateLiquids();
        this.placeStairs();
        this.placeTraps();
        this.placeItems();
        this.placeBuriedItems();
        this.summonPokemon();

        this.placeTeam();
    }

    /** Creates Water, Lava or Air. */
    protected abstract void generateLiquids();

    /** Creates paths between the rooms. */
    protected abstract void generatePaths();

    /** Creates the rooms. */
    protected abstract void generateRooms();

    /** Places buried Items. */
    protected void placeBuriedItems() {
        int items = this.floor.data.buriedItemDensity();
        if (items == 0)
            return;
        items = RandomUtil.randomize(items, this.random);
        if (items < 1)
            items = 1;
        if (items > 63)
            items = 63;
        for (int i = 0; i < items; ++i) {
            Tile tile = this.floor.randomEmptyTile(false, false, TileType.WALL, this.random);
            Item item = this.floor.randomBuriedItem(this.random);
            int quantity = 0;
            if (item.effect() == ItemEffects.Pokedollars)
                quantity = this.floor.getMoneyQuantity();
            else if (item.isStackable)
                quantity = RandomUtil.nextGaussian(10, 7, this.random);

            tile.setItem(new ItemStack(item.id).setQuantity(quantity));
        }
    }

    /** Places Items. */
    protected void placeItems() {
        int items = this.floor.data.itemDensity();
        if (items == 0)
            return;
        items = RandomUtil.randomize(items, this.random);
        if (items < 1)
            items = 1;
        if (items > 63)
            items = 63;
        for (int i = 0; i < items; ++i) {
            Tile tile = this.floor.randomEmptyTile(true, false, TileType.GROUND, this.random);
            ItemStack item = this.floor.dungeon.dungeon().randomItem(this.random, this.floor.id, true);
            tile.setItem(item);
        }
    }

    /** Randomly places the Stairs tile in a random room. */
    protected void placeStairs() {
        this.floor.randomEmptyTile(true, false, this.random).setType(TileType.STAIR);
    }

    protected void placeTeam() {
        Tile t = this.floor.randomRoom(this.random).randomTile(this.random);
        this.floor.teamSpawn = new Point(t.x, t.y);
    }

    /** Places traps. */
    protected void placeTraps() {
        int traps = this.floor.data.trapDensity();
        if (traps == 0)
            return;
        traps = RandomUtil.randomize(traps, this.random);
        if (traps < 1)
            traps = 1;
        if (traps > 63)
            traps = 63;
        for (int i = 0; i < traps; ++i) {
            Tile t = this.floor.randomEmptyTile(true, false, TileType.GROUND, this.random);
            t.trap = this.floor.randomTrap(this.random);
            t.trapRevealed = t.trap == TrapRegistry.WONDER_TILE;
        }
    }

    /** Summons Pokemon. */
    protected void summonPokemon() {
    }

}

package com.darkxell.common.dungeon.floor.layout;

import static com.darkxell.common.dungeon.floor.Floor.*;

import java.util.HashMap;
import java.util.Random;

import javafx.util.Pair;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

/** Represents a Floor's layout. */
public abstract class Layout
{

	private static final HashMap<Integer, Layout> layouts = new HashMap<Integer, Layout>();

	public static final Layout SINGLE_ROOM = new SingleRoomLayout();
	public static final Layout SMALL = new GridRoomsLayout(1, 1, 2, 2, 3, 5, 5, 9, 12);

	/** @return The Layout with the input ID. */
	public static Layout find(int id)
	{
		return layouts.get(id);
	}

	/** Temporary storage for the generating Floor. */
	protected Floor floor;
	/** This Layout's id. */
	public final int id;
	/** The number of Rooms in this Layout. */
	public final int minRooms, maxRooms;
	/** RNG */
	protected Random random;
	/** Temporary storage for the floor's rooms. */
	protected Room[] rooms;
	/** Temporary storage for the floor's tiles. */
	protected Tile[][] tiles;

	public Layout(int id, int rooms)
	{
		this(id, rooms, rooms);
	}

	public Layout(int id, int minRooms, int maxRooms)
	{
		this.id = id;
		this.minRooms = minRooms;
		this.maxRooms = maxRooms;
		layouts.put(this.id, this);
	}

	/** Generates the default layout: Unbreakable walls surrounding breakable walls. */
	public void defaultTiles()
	{
		this.tiles = new Tile[ALL_WIDTH][ALL_HEIGHT];
		for (int x = 0; x < ALL_WIDTH; ++x)
			for (int y = 0; y < ALL_HEIGHT; ++y)
				if (WALKABLE.contains(x, y)) this.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL);
				else this.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL_END);
	}

	/** Generates a Floor.
	 * 
	 * @param floor - The Floor to build.
	 * @return The generated Tiles and Rooms. */
	public Pair<Tile[][], Room[]> generate(Floor floor)
	{
		this.floor = floor;
		this.random = this.floor.random;
		this.generateRooms();
		this.generateTiles();
		this.generatePaths();
		this.generateLiquids();
		this.placeStairs();
		this.placeWonderTiles();
		if (this.floor.dungeon.hasTraps) this.placeTraps();
		this.placeItems();
		this.summonPokemon();

		// Clear temp variables
		Pair<Tile[][], Room[]> toReturn = new Pair<Tile[][], Room[]>(this.tiles, this.rooms);
		this.tiles = null;
		this.rooms = null;
		this.floor = null;
		this.random = null;
		return toReturn;
	}

	/** Creates Water, Lava or Air. */
	protected abstract void generateLiquids();

	/** Creates paths between the rooms. */
	protected void generatePaths()
	{
		// TODO Layout.generatePaths()
	}

	/** Creates the rooms. */
	protected abstract void generateRooms();

	/** Creates default tiles from the Rooms. */
	private void generateTiles()
	{
		this.defaultTiles();
		for (Room room : this.rooms)
			for (Tile tile : room.listTiles())
				tile.setType(TileType.GROUND);
	}

	/** Places Items. */
	private void placeItems()
	{
		// TODO Layout.placeItems()
	}

	/** Randomly places the Stairs tile in a random room. */
	private void placeStairs()
	{
		Room exitRoom = this.randomRoom();
		exitRoom.randomTile(this.random).setType(TileType.STAIR);
	}

	/** Places traps. */
	private void placeTraps()
	{
		// TODO Layout.placeTraps()
	}

	/** Places Wonder Tiles. */
	private void placeWonderTiles()
	{
		int wonder = 2; // Number of wonder tiles to place
		wonder += this.random.nextInt(3) - 2; // wonder = random[wonder-1;wonder+1]
		for (int i = 0; i <= wonder; ++i)
			this.randomRoom().randomTile(this.random, TileType.GROUND).setType(TileType.WONDER_TILE);
	}

	private Room randomRoom()
	{
		return this.rooms[random.nextInt(this.rooms.length)];
	}

	/** @return A random number of Rooms for this Layout. */
	public int randomRoomCount(Random random)
	{
		return random.nextInt(this.maxRooms - this.minRooms + 1) + this.minRooms;
	}

	/** Summons Pokémon. */
	private void summonPokemon()
	{
		// TODO Layout.summonPokemon()
	}

}

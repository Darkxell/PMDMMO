package com.darkxell.common.dungeon.floor.layout;

import static com.darkxell.common.dungeon.floor.Floor.*;

import java.util.Random;

import javafx.util.Pair;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

/** Represents a Floor's layout. */
public abstract class Layout
{

	public static final Layout SINGLE_ROOM = new SingleRoomLayout();

	/** Temporary storage for the generating Floor. */
	protected Floor floor;
	/** This Layout's id. */
	public final int id;
	/** RNG */
	protected Random random;
	/** Temporary storage for the floor's rooms. */
	protected Room[] rooms;
	/** Temporary storage for the floor's tiles. */
	protected Tile[][] tiles;

	public Layout(int id)
	{
		this.id = id;
	}

	/** Generates the default layout: Unbreakable walls surrounding breakable walls. */
	public void defaultTiles()
	{
		this.tiles = new Tile[ALL_WIDTH][ALL_HEIGHT];
		for (int x = 0; x < this.tiles.length; ++x)
			for (int y = 0; y < this.tiles.length; ++y)
				if (x < MIN_X || x > MAX_X || y < MIN_Y || y > MAX_Y) this.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL_END);
				else this.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL);
	}

	/** Generates a Floor.
	 * 
	 * @return An object containing the Floor's tiles and rooms. */
	public Pair<Tile[][], Room[]> generate(Floor floor)
	{
		this.floor = floor;
		this.random = this.floor.random;
		this.rooms = this.generateRooms();
		this.generateTiles();
		this.generatePaths();
		this.generateLiquids();
		this.placeStairs();
		this.placeWonderTiles();
		if (this.floor.dungeon.hasTraps) this.placeTraps();
		this.placeItems();
		this.summonPokemon();

		// Clear temp variables and return
		Pair<Tile[][], Room[]> layout = new Pair<Tile[][], Room[]>(this.tiles, this.rooms);
		this.tiles = null;
		this.rooms = null;
		this.floor = null;
		this.random = null;
		return layout;
	}

	/** Creates Water, Lava or Air. */
	protected abstract void generateLiquids();

	/** Creates paths between the rooms. */
	protected abstract void generatePaths();

	/** Creates the rooms. */
	protected abstract Room[] generateRooms();

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
		Room exitRoom = this.floor.randomRoom(this.random);
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
			this.floor.randomRoom(this.random).randomTile(this.random, TileType.GROUND).setType(TileType.WONDER_TILE);
	}

	/** Summons Pokémon. */
	private void summonPokemon()
	{
		// TODO Layout.summonPokemon()
	}

}

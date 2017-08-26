package com.darkxell.common.dungeon.floor.layout;

import static com.darkxell.common.dungeon.floor.Floor.*;
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
	/** Temporary storage for the floor's rooms. */
	protected Room[] rooms;
	/** Temporary storage for the floor's tiles. */
	protected Tile[][] tiles;

	public Layout(int id)
	{
		this.id = id;
	}

	/** Generates the default layout: Unbreakable walls surrounding breakable walls. */
	public Tile[][] defaultTiles(Floor floor)
	{
		Tile[][] tiles = new Tile[ALL_WIDTH][ALL_HEIGHT];
		for (int x = 0; x < tiles.length; ++x)
			for (int y = 0; y < tiles.length; ++y)
				if (x < MIN_X || x > MAX_X || y < MIN_Y || y > MAX_Y) tiles[x][y] = new Tile(floor, x, y, TileType.WALL_END);
				else tiles[x][y] = new Tile(floor, x, y, TileType.WALL);
		return tiles;
	}

	/** Generates a Floor.
	 * 
	 * @return An object containing the Floor's tiles and rooms. */
	public Pair<Tile[][], Room[]> generate(Floor floor)
	{
		this.floor = floor;
		this.rooms = this.generateRooms();
		this.tiles = this.generateTiles();
		this.placeStairs();
		this.generatePaths();
		this.generateLiquids();
		this.placeWonderTiles();
		if (this.floor.dungeon.hasTraps) this.placeTraps();
		this.placeItems();
		this.summonPokemon();

		// Clear temp variables and return
		Pair<Tile[][], Room[]> layout = new Pair<Tile[][], Room[]>(this.tiles, this.rooms);
		this.tiles = null;
		this.rooms = null;
		this.floor = null;
		return layout;
	}

	/** Creates Water, Lava or Air. */
	protected abstract void generateLiquids();

	/** Creates paths between the rooms. */
	protected abstract void generatePaths();

	/** Creates the rooms. */
	protected abstract Room[] generateRooms();

	/** Creates default tiles from the Rooms. */
	private Tile[][] generateTiles()
	{
		// TODO Layout.generateTiles()
		return null;
	}

	/** Places Items. */
	private void placeItems()
	{
		// TODO Layout.placeItems()
	}

	/** Places the Stairs tile. */
	private void placeStairs()
	{
		// TODO Layout.placeStairs()
	}

	/** Places traps. */
	private void placeTraps()
	{
		// TODO Layout.placeTraps()
	}

	/** Places Wonder Tiles. */
	private void placeWonderTiles()
	{
		// TODO Layout.placeWonderTiles()
	}

	/** Summons Pokémon. */
	private void summonPokemon()
	{
		// TODO Layout.summonPokemon()
	}

}

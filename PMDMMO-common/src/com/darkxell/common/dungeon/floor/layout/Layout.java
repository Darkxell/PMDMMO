package com.darkxell.common.dungeon.floor.layout;

import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.TileType;

/** Represents a Floor's layout. */
public abstract class Layout {

	/** @return A new layout from the input ID. */
	public static Layout find(int id) {
		switch (id) {
		case 0:
			return new StaticLayout();
		case 1:
			return new SingleRoomLayout();
		case 2:
			return new GridRoomsLayout(id, 2, 2, 5, 5, 14, 14);
		default:
			return null;
		}
	}

	/** Temporary storage for the generating Floor. */
	protected Floor floor;
	/** RNG */
	protected Random random;

	/**
	 * Generates a Floor.
	 * 
	 * @param floor
	 *            - The Floor to build.
	 * @return The generated Tiles and Rooms.
	 */
	public void generate(Floor floor) {
		this.floor = floor;
		this.random = this.floor.dungeon.random;
		this.generateRooms();
		this.generatePaths();
		this.generateLiquids();
		this.placeStairs();
		this.placeWonderTiles();
		if (this.floor.dungeon.dungeon().hasTraps)
			this.placeTraps();
		this.placeItems();
		this.summonPokemon();

		this.placeTeam();
	}

	/** Creates the rooms. */
	protected abstract void generateRooms();

	/** Creates paths between the rooms. */
	protected abstract void generatePaths();

	/** Creates Water, Lava or Air. */
	protected abstract void generateLiquids();

	/** Randomly places the Stairs tile in a random room. */
	protected void placeStairs() {
		Room exitRoom = this.floor.randomRoom(this.random);
		exitRoom.randomTile(this.random).setType(TileType.STAIR);
	}

	/** Places Wonder Tiles. */
	protected void placeWonderTiles() {
		int wonder = 2; // Number of wonder tiles to place
		wonder += this.random.nextInt(3) - 2; // wonder =
												// random[wonder-1;wonder+1]
		for (int i = 0; i <= wonder; ++i)
			this.floor.randomRoom(this.random).randomTile(this.random, TileType.GROUND).setType(TileType.WONDER_TILE);
	}

	/** Places traps. */
	protected abstract void placeTraps();

	/** Places Items. */
	protected abstract void placeItems();

	protected abstract void placeTeam();

	/** Summons Pokémon. */
	protected abstract void summonPokemon();

}

package com.darkxell.common.dungeon.floor.layout;

import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.trap.TrapRegistry;

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

	/** Places traps. */
	protected void placeTraps()
	{
		int traps = this.floor.data.trapDensity(); // Number of traps to place
		if (traps == 0) return;
		traps += this.random.nextInt(3) - 2; // traps = random[traps-1;traps+1]
		if (traps < 0) traps = 0;
		if (traps > 63) traps = 63;
		for (int i = 0; i < traps; ++i)
		{
			Tile t = this.floor.randomEmptyTile(true, this.random);
			t.trap = this.floor.randomTrap(this.random);
			t.trapRevealed = t.trap == TrapRegistry.WONDER_TILE;
		}

	}

	/** Places Items. */
	protected abstract void placeItems();

	protected abstract void placeTeam();

	/** Summons Pokémon. */
	protected abstract void summonPokemon();

}

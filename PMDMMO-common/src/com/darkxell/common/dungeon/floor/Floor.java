package com.darkxell.common.dungeon.floor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Represents a generated Floor in a Dungeon. */
public class Floor {

	/** This Floor's Dungeon. */
	public final DungeonInstance dungeon;
	/** This Floor's ID. */
	public final int id;
	/** This Floor's layout. */
	public final Layout layout;
	/** RNG for game logic: moves, mob spawning, etc. */
	public final Random random;
	/** This Floor's rooms. null before generating. */
	public Room[] rooms;
	/** The position at which the team will spawn. */
	public Point teamSpawn;
	/**
	 * This Floor's tiles. null before generating. Note that this array must NOT
	 * be modified. It is only public because the generation algorithm uses this
	 * array to generate the floor.
	 */
	public Tile[][] tiles;
	private boolean isGenerating = true;;

	public Floor(int id, Layout layout, DungeonInstance dungeon, Random random) {
		this.id = id;
		this.dungeon = dungeon;
		this.layout = layout;
		this.random = random;
	}

	/** Generates this Floor. */
	public void generate() {
		this.layout.generate(this);
		isGenerating = false;
		for (Tile[] row : this.tiles)
			for (Tile t : row)
				t.updateNeighbors();
	}

	/** @return True if this Floor is done generating. */
	public boolean isGenerated() {
		return !isGenerating ;
	}

	/** @return A random Room in this Floor. */
	public Room randomRoom(Random random) {
		return this.rooms[random.nextInt(this.rooms.length)];
	}

	/**
	 * @return The room at the input X, Y coordinates. null if not in a Room.
	 */
	public Room roomAt(int x, int y) {
		for (Room room : this.rooms)
			if (room.contains(x, y))
				return room;
		return null;
	}

	public int getWidth() {
		return this.tiles.length;
	}

	public int getHeight() {
		return this.tiles[0].length;
	}
	
	public void summonPokemon(DungeonPokemon pokemon, int x, int y) {
		if (!(this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length))
			this.tileAt(x, y).setPokemon(pokemon);
		this.dungeon.registerActor(pokemon);
	}

	/** @return The tile at the input X, Y coordinates. */
	public Tile tileAt(int x, int y) {
		if (this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length)
			return null;
		return this.tiles[x][y];
	}

	@Override
	public String toString() {
		String s = "";
		for (int y = 0; y < this.getHeight(); ++y) {
			for (int x = 0; x < this.getWidth(); ++x) {
				Tile t = this.tileAt(x, y);
				s += t != null ? t.type().c : "?";
			}
			s += "\n";
		}
		return s;
	}

	/** Overrides all of the floor's tiles. */
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public ArrayList<DungeonEvent> onTurnStart()
	{
		ArrayList<DungeonEvent> e = new ArrayList<DungeonEvent>();
		return e;
	}
}

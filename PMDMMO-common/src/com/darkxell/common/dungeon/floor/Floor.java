package com.darkxell.common.dungeon.floor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import com.darkxell.common.ai.AIManager;
import com.darkxell.common.dungeon.DungeonEncounter;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.DungeonItem;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.pokemon.PokemonSpawnedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Directions;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.weather.Weather;
import com.darkxell.common.weather.WeatherInstance;

/** Represents a generated Floor in a Dungeon. */
public class Floor
{

	/** Stores all AI objects for Pok�mon on this Floor. */
	public final AIManager aiManager;
	/** This Floor's data. */
	public final FloorData data;
	/** This Floor's Dungeon. */
	public final DungeonInstance dungeon;
	/** This Floor's ID. */
	public final int id;
	private boolean isGenerating = true;
	/** This Floor's layout. */
	public final Layout layout;
	/** The number of turns until a Pok�mon spawns. */
	private int nextSpawn;
	/** RNG for game logic: moves, mob spawning, etc. */
	public final Random random;
	/** This Floor's rooms. null before generating. */
	public Room[] rooms;
	/** The position at which the team will spawn. */
	public Point teamSpawn;
	/** This Floor's tiles. null before generating. Note that this array must NOT be modified. It is only public because the generation algorithm uses this array to generate the floor. */
	public Tile[][] tiles;
	/** List of Weather conditions applied to this Floor. */
	private final ArrayList<WeatherInstance> weatherCondition;

	public Floor(int id, Layout layout, DungeonInstance dungeon, Random random)
	{
		this.id = id;
		this.dungeon = dungeon;
		this.data = this.dungeon.dungeon().getData(this.id);
		this.layout = layout;
		this.random = random;
		this.weatherCondition = new ArrayList<WeatherInstance>();
		this.aiManager = new AIManager(this);
	}

	/** @param weather - The weather to add.
	 * @return The Weather Changed Event if the Weather changes. */
	public WeatherChangedEvent addWeather(WeatherInstance weather)
	{
		WeatherInstance previous = this.currentWeather();
		if (!this.weatherCondition.contains(weather))
		{
			this.weatherCondition.add(weather);
			this.weatherCondition.sort(Comparator.naturalOrder());
		}
		WeatherInstance next = this.currentWeather();
		if (previous.weather == next.weather) return null;
		return new WeatherChangedEvent(this, previous, next);
	}

	/** @return True if the input Tile connects to a path outside a Room (considering that Tile is in a Room, which is not tested in this method). */
	public boolean connectsToPath(Tile tile)
	{
		for (short direction : new short[] { Directions.NORTH, Directions.EAST, Directions.SOUTH, Directions.WEST })
			if (!tile.adjacentTile(direction).isInRoom() && tile.adjacentTile(direction).type() == TileType.GROUND) return true;
		return false;
	}

	private int countWildPokemon()
	{
		int count = 0;
		for (DungeonPokemon p : this.listPokemon())
			if (p.pokemon.player == null) ++count;
		return count;
	}

	public WeatherInstance currentWeather()
	{
		if (this.weatherCondition.size() == 0) return new WeatherInstance(Weather.CLEAR, null, 0, this, 0);
		return this.weatherCondition.get(0);
	}

	/** Clears unnecessary data of this Floor. */
	public void dispose()
	{
		for (Tile[] row : this.tiles)
			for (Tile tile : row)
				if (tile.getPokemon() != null && tile.getPokemon().pokemon.player == null) tile.getPokemon().dispose();
	}

	/** Generates this Floor. */
	public void generate()
	{
		this.layout.generate(this);
		isGenerating = false;
		for (Tile[] row : this.tiles)
			for (Tile t : row)
				t.updateNeighbors();
	}

	public int getHeight()
	{
		return this.tiles[0].length;
	}

	public int getWidth()
	{
		return this.tiles.length;
	}

	/** @return True if this Floor is done generating. */
	public boolean isGenerated()
	{
		return !isGenerating;
	}

	public ArrayList<DungeonPokemon> listPokemon()
	{
		ArrayList<DungeonPokemon> pokemon = new ArrayList<DungeonPokemon>();
		if (this.tiles != null) for (Tile[] row : this.tiles)
			for (Tile t : row)
				if (t.getPokemon() != null && !pokemon.contains(t.getPokemon())) pokemon.add(t.getPokemon());
		return pokemon;
	}

	/** Called when a this Floor starts. */
	public ArrayList<DungeonEvent> onFloorStart()
	{
		ArrayList<DungeonEvent> e = new ArrayList<DungeonEvent>();
		Weather w = this.dungeon.dungeon().weather(this.id, this.random);
		for (DungeonPokemon pokemon : this.listPokemon())
			e.addAll(pokemon.onFloorStart(this));
		e.add(new WeatherCreatedEvent(new WeatherInstance(w, null, 0, this, -1)));
		return e;
	}

	/** Called when a new turn starts. */
	public ArrayList<DungeonEvent> onTurnStart()
	{
		ArrayList<DungeonEvent> e = new ArrayList<DungeonEvent>();
		// e.add(new MessageEvent(this, new Message("New turn!", false)));

		// For each existing Pok�mon
		for (DungeonPokemon pokemon : this.listPokemon())
			e.addAll(pokemon.onTurnStart(this));

		// Weather
		for (int w = this.weatherCondition.size() - 1; w >= 0; --w)
			e.addAll(this.weatherCondition.get(w).update());

		// Pok�mon spawning
		if (this.data.pokemonDensity() > this.countWildPokemon())
		{
			if (this.nextSpawn <= 0)
			{
				DungeonEncounter s = this.dungeon.dungeon().randomEncounter(this.random, this.id);
				if (s != null)
				{
					DungeonPokemon wild = new DungeonPokemon(s.pokemon().generate(this.random, s.level));
					Tile tile = this.randomEmptyTile(true, true, this.random);
					if (tile != null)
					{
						e.add(new PokemonSpawnedEvent(this, wild, tile));
						this.nextSpawn = RandomUtil.nextIntInBounds(50, 100, this.random) / this.data.pokemonDensity();
					}
				}
			} else--this.nextSpawn;
		}

		return e;
	}

	public Item randomBuriedItem(Random random)
	{
		HashMap<DungeonItem, Integer> items = this.dungeon.dungeon().buriedItems(this.id);
		return randomItem(items, random);
	}

	/** @param inRoom - True if the Tile should be in a Room (will also avoid tiles adjacent to corridors in rooms).
	 * @param awayFromPlayers - True if the Tile should be far away from players (to avoid spawning a pok�mon close to a player for example).
	 * @return A Random Tile in this floor. */
	public Tile randomEmptyTile(boolean inRoom, boolean awayFromPlayers, Random random)
	{
		return this.randomEmptyTile(inRoom, awayFromPlayers, null, random);
	}

	/** @param inRoom - True if the Tile should be in a Room (will also avoid tiles adjacent to corridors in rooms).
	 * @param awayFromPlayers - True if the Tile should be far away from players (to avoid spawning a pok�mon close to a player for example).
	 * @param type - A Type that the Tile has to match. null for any type.
	 * @return A Random Tile in this floor. */
	public Tile randomEmptyTile(boolean inRoom, boolean awayFromPlayers, TileType type, Random random)
	{
		ArrayList<Tile> candidates = new ArrayList<Tile>();
		if (inRoom) for (Room room : this.rooms)
			candidates.addAll(room.listTiles());
		else
		{
			for (Tile[] row : this.tiles)
				for (Tile tile : row)
					candidates.add(tile);
		}

		if (type != null) candidates.removeIf(new Predicate<Tile>() {
			@Override
			public boolean test(Tile t)
			{
				return t.type() != type;
			}
		});

		candidates.removeIf(new Predicate<Tile>() {
			@Override
			public boolean test(Tile t)
			{
				return !t.isEmpty();
			}
		});

		if (inRoom) candidates.removeIf(new Predicate<Tile>() {
			@Override
			public boolean test(Tile t)
			{
				return connectsToPath(t);
			}
		});

		if (awayFromPlayers)
		{
			ArrayList<DungeonPokemon> players = new ArrayList<DungeonPokemon>(this.listPokemon());
			players.removeIf((DungeonPokemon p) -> {
				return p.pokemon.player == null;
			});
			candidates.removeIf((Tile t) -> {
				for (DungeonPokemon player : players)
					if (Math.abs(t.x - player.tile().x) < 10 && Math.abs(t.y - player.tile().y) < 7) return true;
				return false;
			});
		}

		return RandomUtil.random(candidates, random);
	}

	public Item randomItem(HashMap<DungeonItem, Integer> items, Random random)
	{
		DungeonItem itemGroup = RandomUtil.weightedRandom(items, random);
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<Integer> chances = new ArrayList<Integer>();
		for (int i = 0; i < itemGroup.items.length; ++i)
		{
			ids.add(itemGroup.items[i]);
			chances.add(itemGroup.chances[i]);
		}
		return ItemRegistry.find(RandomUtil.weightedRandom(ids, chances, random));
	}

	public Item randomItem(Random random)
	{
		HashMap<DungeonItem, Integer> items = this.dungeon.dungeon().items(this.id);
		return randomItem(items, random);
	}

	/** @return A random Room in this Floor. */
	public Room randomRoom(Random random)
	{
		return this.rooms[random.nextInt(this.rooms.length)];
	}

	/** @return A Random Trap in this Floor. */
	public Trap randomTrap(Random random)
	{
		HashMap<Integer, Integer> traps = this.dungeon.dungeon().traps(this.id);
		return TrapRegistry.find(RandomUtil.weightedRandom(traps, random));
	}

	/** @param weather - The weather to clean.
	 * @return The Weather Changed Event if the Weather changes. */
	public WeatherChangedEvent removeWeather(WeatherInstance weather)
	{
		WeatherInstance previous = this.currentWeather();
		if (this.weatherCondition.size() > 0 && this.weatherCondition.contains(weather)) this.weatherCondition.remove(weather);
		WeatherInstance next = this.currentWeather();
		if (previous == next) return null;
		return new WeatherChangedEvent(this, previous, next);
	}

	public Room room(Tile tile)
	{
		return this.roomAt(tile.x, tile.y);
	}

	/** @return The room at the input X, Y coordinates. null if not in a Room. */
	public Room roomAt(int x, int y)
	{
		for (Room room : this.rooms)
			if (room.contains(x, y)) return room;
		return null;
	}

	/** Overrides all of the floor's tiles. */
	public void setTiles(Tile[][] tiles)
	{
		this.tiles = tiles;
	}

	public ArrayList<DungeonEvent> summonPokemon(DungeonPokemon pokemon, int x, int y)
	{
		if (!(this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length)) this.tileAt(x, y).setPokemon(pokemon);
		this.dungeon.registerActor(pokemon);
		if (!pokemon.isTeamLeader()) this.aiManager.register(pokemon);
		return pokemon.onFloorStart(this);
	}

	/** @return The tile at the input X, Y coordinates. */
	public Tile tileAt(int x, int y)
	{
		if (this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length) return null;
		return this.tiles[x][y];
	}

	@Override
	public String toString()
	{
		String s = "";
		for (int y = 0; y < this.getHeight(); ++y)
		{
			for (int x = 0; x < this.getWidth(); ++x)
			{
				Tile t = this.tileAt(x, y);
				s += t != null ? t.type().c : "?";
			}
			s += "\n";
		}
		return s;
	}

	public void unsummonPokemon(DungeonPokemon pokemon)
	{
		pokemon.tile().setPokemon(null);
		this.dungeon.unregisterActor(pokemon);
		this.aiManager.unregister(pokemon);
	}

}

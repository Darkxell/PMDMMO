package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/** The travel of Pokémon. */
public class PokemonTravelEvent extends DungeonEvent implements Communicable
{

	private Direction direction;
	private Tile origin, destination;
	private DungeonPokemon pokemon;
	private boolean running;

	public PokemonTravelEvent(Floor floor)
	{
		super(floor);
	}

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, boolean running, Direction direction)
	{
		super(floor, pokemon);
		this.pokemon = pokemon;
		this.running = running;
		this.direction = direction;
		this.origin = pokemon.tile();
		this.destination = pokemon.tile().adjacentTile(this.direction);
		if (!this.destination.canMoveTo(this.pokemon, this.direction, true))
		{
			Logger.e("Illegal movement!");
			this.displayInConsole();
		}
	}

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, Direction direction)
	{
		this(floor, pokemon, false, direction);
	}

	public Tile destination()
	{
		return this.destination;
	}

	public Direction direction()
	{
		return this.direction;
	}

	private void displayInConsole()
	{
		System.out.println(this.direction);
		Floor floor = this.origin.floor;
		int xs = this.origin.x, ys = this.origin.y;
		for (int x = xs - 2; x <= xs + 2; ++x)
		{
			for (int y = ys - 2; y <= ys + 2; ++y)
				if (floor.tileAt(x, y) == this.origin) System.out.print("O");
				else if (floor.tileAt(x, y).getPokemon() != null) System.out.print("P");
				else System.out.print(floor.tileAt(x, y).type().c);
			System.out.println();
		}
	}

	public boolean isReversed(PokemonTravelEvent t)
	{
		return this.origin == t.destination && this.destination == t.origin;
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + " travelled.";
	}

	public Tile origin()
	{
		return this.origin;
	}

	public DungeonPokemon pokemon()
	{
		return this.pokemon;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.pokemon.isTeamLeader()) this.resultingEvents.add(new BellyChangedEvent(this.floor, this.pokemon, -.1 * this.pokemon.energyMultiplier()));
		this.origin.removePokemon(this.pokemon);
		this.destination.setPokemon(this.pokemon);
		this.resultingEvents.addAll(this.destination.onPokemonStep(this.floor, this.pokemon, this.running));
		return super.processServer();
	}

	@Override
	public void read(JsonObject value) throws JsonReadingException
	{
		Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("pokemon", 0));
		try
		{
			this.direction = Direction.valueOf(value.getString("direction", Direction.NORTH.name()));
		} catch (IllegalArgumentException e)
		{
			throw new JsonReadingException("No direction with name " + value.getString("direction", "null"));
		}
		if (p == null) throw new JsonReadingException("No pokemon with ID " + value.getLong("pokemon", 0));
		this.pokemon = this.actor = p.getDungeonPokemon();
		try
		{
			this.running = value.getBoolean("running", false);
		} catch (IllegalArgumentException e)
		{
			throw new JsonReadingException("Wrong value for running: " + value.get("running"));
		}
		this.origin = pokemon.tile();
		this.destination = pokemon.tile().adjacentTile(this.direction);
	}

	public boolean running()
	{
		return this.running;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("actor", this.pokemon.id());
		root.add("running", this.running);
		root.add("direction", this.direction.name());
		return root;
	}

	@Override
	public String toString()
	{
		return this.pokemon.toString() + " travels";
	}

}

package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Direction;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class PokemonRotateEvent extends DungeonEvent implements Communicable
{

	private Direction direction;
	private DungeonPokemon pokemon;
	
	public PokemonRotateEvent(Floor floor)
	{
		super(floor);
	}

	/** @param pokemon - The Pokémon that rotates.
	 * @param direction - The new direction the Pokémon should face. */
	public PokemonRotateEvent(Floor floor, DungeonPokemon pokemon, Direction direction)
	{
		super(floor);
		this.pokemon = pokemon;
		this.direction = direction;
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + " rotated to face " + this.direction.getName();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.pokemon.setFacing(this.direction);
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
		this.pokemon = p.getDungeonPokemon();
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("actor", this.pokemon.id());
		root.add("direction", this.direction.name());
		return root;
	}

}

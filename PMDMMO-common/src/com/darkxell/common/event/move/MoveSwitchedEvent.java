package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class MoveSwitchedEvent extends DungeonEvent implements Communicable
{

	/** The indices of the switched moves. */
	private int from, to;
	/** The Pokémon whose moves are switched. */
	private Pokemon pokemon;

	public MoveSwitchedEvent(Floor floor)
	{
		super(floor);
	}

	public MoveSwitchedEvent(Floor floor, Pokemon pokemon, int from, int to)
	{
		super(floor);
		this.pokemon = pokemon;
		this.from = from;
		this.to = to;
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + "switched moves " + this.from + " and " + this.to;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.pokemon.switchMoves(this.from, this.to);
		return super.processServer();
	}

	@Override
	public void read(JsonObject value) throws JsonReadingException
	{
		this.pokemon = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("pokemon", 0));
		if (this.pokemon == null) throw new JsonReadingException("No pokemon with ID " + value.getLong("pokemon", 0));
		try
		{
			this.from = value.getInt("from", 0);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for from: " + value.get("from"));
		}
		try
		{
			this.to = value.getInt("to", 0);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for to: " + value.get("to"));
		}
		if (this.from == this.to) throw new JsonReadingException("From and to can't be the same indices.");
	}

	@Override
	public JsonObject toJson()
	{
		return Json.object().add("pokemon", this.pokemon.id()).add("from", this.from).add("to", this.to);
	}

}

package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class PokemonRotateEvent extends DungeonEvent
{

	public final Direction direction;
	public final DungeonPokemon pokemon;

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

}

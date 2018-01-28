package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class PokemonRotateEvent extends DungeonEvent
{

	public final Direction direction;
	public final DungeonPokemon pokemon;

	public PokemonRotateEvent(Floor floor, DungeonPokemon pokemon, Direction direction)
	{
		this(floor, pokemon, direction, false);
	}

	/** @param pokemon - The Pok�mon that rotates.
	 * @param direction - The new direction the Pok�mon should face.
	 * @param consumesTurn - True if this Action consumes the Pok�mon's turn. Default is false. */
	public PokemonRotateEvent(Floor floor, DungeonPokemon pokemon, Direction direction, boolean consumesTurn)
	{
		super(floor, consumesTurn ? pokemon : null);
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

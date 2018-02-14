package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.Pokemon;

public class MoveSwitchedEvent extends DungeonEvent
{

	/** The indices of the switched moves. */
	public final int from, to;
	/** The Pokémon whose moves are switched. */
	public final Pokemon pokemon;

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

}

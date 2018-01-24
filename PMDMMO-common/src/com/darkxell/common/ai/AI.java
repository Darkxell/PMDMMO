package com.darkxell.common.ai;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public abstract class AI
{

	/** The Floor context. */
	public final Floor floor;
	/** The Pokémon this AI controls. */
	public final DungeonPokemon pokemon;

	public AI(Floor floor, DungeonPokemon pokemon)
	{
		this.floor = floor;
		this.pokemon = pokemon;
	}

	public abstract DungeonEvent takeAction();

}

package com.darkxell.common.ai;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.TurnSkippedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public abstract class AI
{

	public static abstract class AIState
	{
		public final AI ai;

		public AIState(AI ai)
		{
			this.ai = ai;
		}

		public abstract DungeonEvent takeAction();
	}

	/** The Floor context. */
	public final Floor floor;
	/** The Pokémon this AI controls. */
	public final DungeonPokemon pokemon;
	protected AIState state;

	public AI(Floor floor, DungeonPokemon pokemon)
	{
		this.floor = floor;
		this.pokemon = pokemon;
	}

	public DungeonEvent takeAction()
	{
		if (this.state == null) return new TurnSkippedEvent(this.floor, this.pokemon);
		return this.state.takeAction();
	}

}

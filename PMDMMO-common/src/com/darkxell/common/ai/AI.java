package com.darkxell.common.ai;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

/** Class that controls Pokemon not controlled by players in Dungeons.<br>
 * This abstract class should be extended for each type of Pokemon, i.e. wilds, allies, bosses...<br>
 * This class does not determine actions but sets {@link AIState}s that are then called to determine those actions. */
public abstract class AI
{

	/** Class representing a State in a Pokemon's AI. Determines which action to take. */
	public static abstract class AIState
	{
		/** Reference to the parent AI. */
		public final AI ai;

		public AIState(AI ai)
		{
			this.ai = ai;
		}

		/** Called at the end of each turn so that a Pokemon may rotate.
		 * 
		 * @return <code>null</code> if no rotation, else the new direction to face. */
		public Direction mayRotate()
		{
			return null;
		}

		public abstract DungeonEvent takeAction();
	}

	/** The Floor context. */
	public final Floor floor;
	/** The {@link DungeonPokemon Pokemon} this AI controls. */
	public final DungeonPokemon pokemon;
	/** The current {@link AIState State} of this AI. May be null. */
	protected AIState state;

	public AI(Floor floor, DungeonPokemon pokemon)
	{
		this.floor = floor;
		this.pokemon = pokemon;
		this.state = this.defaultState();
	}

	public abstract AIState defaultState();

	/** Called at the end of each turn. Allows the Pokemon to rotate.
	 * 
	 * @return The Direction to rotate to. */
	public Direction mayRotate()
	{
		if (this.state == null) return null;
		return this.state.mayRotate();
	}

	/** Calls the AIState to determine the action to execute. */
	public DungeonEvent takeAction()
	{
		this.update();
		if (this.state == null) return new TurnSkippedEvent(this.floor, this.pokemon);
		return this.state.takeAction();
	}

	/** Changes the AIState depending on the current situation. */
	protected abstract void update();

}

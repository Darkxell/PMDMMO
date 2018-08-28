package com.darkxell.common.ai;

import com.darkxell.common.ai.states.AIStateWanderAround;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

/** AI for wild Pokemon. */
public class RescueableAI extends AI
{

	public RescueableAI(Floor floor, DungeonPokemon pokemon)
	{
		super(floor, pokemon);
	}

	@Override
	protected void update()
	{}

	@Override
	public AIState defaultState()
	{
		return new AIStateWanderAround(this);
	}

}

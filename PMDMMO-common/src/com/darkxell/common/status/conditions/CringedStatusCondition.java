package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusCondition;

public class CringedStatusCondition extends StatusCondition
{

	public CringedStatusCondition(int id, int durationMin, int durationMax)
	{
		super(id, durationMin, durationMax);
	}
	
	@Override
	public boolean preventsUsingMove(LearnedMove move, DungeonPokemon pokemon, Floor floor)
	{
		return true;
	}

}

package com.darkxell.common.status;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class PreventsActionStatusCondition extends StatusCondition
{

	public PreventsActionStatusCondition(int id, int durationMin, int durationMax)
	{
		super(id, durationMin, durationMax);
	}

	@Override
	public boolean preventsMoving(DungeonPokemon pokemon, Floor floor)
	{
		return true;
	}

	@Override
	public boolean preventsUsingMoves(DungeonPokemon pokemon, Floor floor)
	{
		return true;
	}

}

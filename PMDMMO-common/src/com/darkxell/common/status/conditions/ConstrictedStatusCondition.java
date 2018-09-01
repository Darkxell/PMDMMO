package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class ConstrictedStatusCondition extends PeriodicDamageStatusCondition
{

	public ConstrictedStatusCondition(int id, int durationMin, int durationMax, int period, int damage)
	{
		super(id, durationMin, durationMax, period, damage);
	}
	
	@Override
	public boolean preventsMoving(DungeonPokemon pokemon, Floor floor)
	{
		return true;
	}

}

package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.UserLevelDamageCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class UserLevelDamageEffect extends MoveEffect
{

	public UserLevelDamageEffect(int id)
	{
		super(id);
	}

	@Override
	protected MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags)
	{
		return new UserLevelDamageCalculator(usedMove, target, floor, flags);
	}

}

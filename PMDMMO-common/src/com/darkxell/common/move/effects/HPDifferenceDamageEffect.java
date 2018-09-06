package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.HPDifferenceCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class HPDifferenceDamageEffect extends MoveEffect
{

	public HPDifferenceDamageEffect(int id)
	{
		super(id);
	}
	
	@Override
	protected MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags)
	{
		return new HPDifferenceCalculator(usedMove, target, floor, flags);
	}

}

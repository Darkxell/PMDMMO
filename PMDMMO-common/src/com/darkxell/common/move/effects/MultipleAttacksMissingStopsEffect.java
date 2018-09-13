package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;

public class MultipleAttacksMissingStopsEffect extends MultipleAttacksEffect
{

	public MultipleAttacksMissingStopsEffect(int id, int attacksMin, int attacksMax)
	{
		super(id, attacksMin, attacksMax);
	}

	@Override
	protected String descriptionID()
	{
		return super.descriptionID() + "_missingstops";
	}

	@Override
	protected boolean shouldContinue(int attacksleft, MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator,
			boolean missed, MoveEvents effects)
	{
		return super.shouldContinue(attacksleft, usedMove, target, flags, floor, calculator, missed, effects) && !missed;
	}

}

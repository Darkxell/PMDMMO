package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.StoredDamageCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageEffect extends MoveEffect
{

	public StoredDamageEffect(int id)
	{
		super(id);
	}

	@Override
	protected MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags)
	{
		return new StoredDamageCalculator(usedMove, target, floor, flags);
	}

	@Override
	public void prepareUse(MoveUse move, Floor floor, ArrayList<DungeonEvent> events)
	{
		super.prepareUse(move, floor, events);
		AppliedStatusCondition storer = move.user.getStatusCondition(StatusConditions.Bide);
		if (storer == null) Logger.e("Pokemon used " + move.move.move().name() + " but had no Bide status!");
		else storer.addFlag("attacked");
	}

}

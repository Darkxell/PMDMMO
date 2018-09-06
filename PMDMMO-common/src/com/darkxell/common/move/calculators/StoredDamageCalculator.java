package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageCalculator extends MoveEffectCalculator
{

	public StoredDamageCalculator(MoveUse move, DungeonPokemon target, Floor floor, String[] flags)
	{
		super(move, target, floor, flags);
	}

	@Override
	public int compute(ArrayList<DungeonEvent> events)
	{
		AppliedStatusCondition storer = this.move.user.getStatusCondition(StatusConditions.Bide);
		if (storer == null)
		{
			Logger.e("Pokemon used " + this.move().name() + " but had no Bide status!");
			return super.compute(events);
		}

		String[] flags = storer.listFlags();
		int stored = 0;
		for (String flag : flags)
			if (flag.startsWith("damage")) stored += Integer.parseInt(flag.split(":")[1]);
		return stored * 2;
	}

}

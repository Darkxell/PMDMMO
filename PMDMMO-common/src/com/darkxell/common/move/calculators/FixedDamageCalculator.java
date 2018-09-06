package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class FixedDamageCalculator extends MoveEffectCalculator
{

	public final int damage;

	public FixedDamageCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags, int damage)
	{
		super(usedMove, target, floor, flags);
		this.damage = damage;
	}

	@Override
	public int compute(ArrayList<DungeonEvent> events)
	{
		return this.damage;
	}

}

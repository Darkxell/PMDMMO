package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.FixedDamageCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class FixedDamageEffect extends MoveEffect
{

	public final int damage;

	public FixedDamageEffect(int id, int damage)
	{
		super(id);
		this.damage = damage;
	}
	
	@Override
	public MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags)
	{
		return new FixedDamageCalculator(usedMove, target, floor, flags, 65);
	}

}

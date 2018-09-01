package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.RandomUtil;

public class MultipleAttacksEffect extends MoveEffect
{

	public final int attacksMin, attacksMax;

	public MultipleAttacksEffect(int id, int attacksMin, int attacksMax)
	{
		super(id);
		this.attacksMin = attacksMin;
		this.attacksMax = attacksMax;
	}

	@Override
	protected void useOn(MoveUse move, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		int attacks = RandomUtil.nextIntInBounds(this.attacksMin, this.attacksMax, floor.random);
		for (int i = 0; i < attacks; ++i)
			super.useOn(move, target, floor, events);
	}

}

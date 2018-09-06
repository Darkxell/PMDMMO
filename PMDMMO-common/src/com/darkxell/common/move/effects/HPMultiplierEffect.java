package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;

public class HPMultiplierEffect extends MoveEffect
{

	public HPMultiplierEffect(int id)
	{
		super(id);
	}

	@Override
	public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags, ArrayList<DungeonEvent> events)
	{
		double hp = move.user.getHpPercentage();
		if (hp < 25) return 8;
		if (hp < 50) return 4;
		if (hp < 75) return 2;
		return super.damageMultiplier(move, target, isUser, floor, flags, events);
	}

}

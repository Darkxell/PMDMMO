package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DoubleDamageEffect extends MoveEffect
{

	public DoubleDamageEffect(int id)
	{
		super(id);
	}

	@Override
	public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags, ArrayList<DungeonEvent> events)
	{
		return 2;
	}

}

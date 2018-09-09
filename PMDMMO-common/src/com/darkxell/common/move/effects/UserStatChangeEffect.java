package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class UserStatChangeEffect extends StatChangeEffect
{

	public UserStatChangeEffect(int id, Stat stat, int stage, int probability)
	{
		super(id, stat, stage, probability);
	}

	@Override
	protected DungeonPokemon pokemonToChange(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator,
			boolean missed, MoveEvents effects)
	{
		return usedMove.user;
	}

}

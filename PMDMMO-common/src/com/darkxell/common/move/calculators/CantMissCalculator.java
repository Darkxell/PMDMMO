package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class CantMissCalculator extends MoveEffectCalculator
{

	public CantMissCalculator(MoveUse move, DungeonPokemon target, Floor floor)
	{
		super(move, target, floor);
	}

	@Override
	public boolean misses(ArrayList<DungeonEvent> events)
	{
		return false;
	}

}

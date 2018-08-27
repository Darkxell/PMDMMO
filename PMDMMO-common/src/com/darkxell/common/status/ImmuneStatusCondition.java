package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class ImmuneStatusCondition extends StatusCondition
{

	public ImmuneStatusCondition(int id, int durationMin, int durationMax)
	{
		super(id, durationMin, durationMax);
	}

	@Override
	public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (stat == Stat.Accuracy && !isUser && target != move.user) return 0;
		return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
	}

}

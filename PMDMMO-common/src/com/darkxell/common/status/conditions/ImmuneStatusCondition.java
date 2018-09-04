package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.pokemon.DungeonPokemon;

public class ImmuneStatusCondition extends StatusCondition
{

	public ImmuneStatusCondition(int id, boolean isAilment, int durationMin, int durationMax)
	{
		super(id, isAilment, durationMin, durationMax);
	}

	@Override
	public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (stat == Stat.Accuracy && !isUser && target != move.user) return 0;
		return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
	}

}

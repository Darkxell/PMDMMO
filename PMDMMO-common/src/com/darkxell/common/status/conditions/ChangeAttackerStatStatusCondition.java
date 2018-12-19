package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class ChangeAttackerStatStatusCondition extends StatusCondition
{

	/** The multiplier to apply (default to 1 for no change). */
	public final double multiply;
	/** The stage change to apply (default to 0 for no change). */
	public final int stage;
	/** The stat to modify. */
	public final Stat stat;

	public ChangeAttackerStatStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, Stat stat, int stage, double multiply)
	{
		super(id, isAilment, durationMin, durationMax);
		this.stat = stat;
		this.stage = stage;
		this.multiply = multiply;
	}

	@Override
	public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (stat == this.stat && !isUser) return value * this.multiply;
		return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
	}

	@Override
	public int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (stat == this.stat && !isUser) return stage + this.stage;
		return super.applyStatStageModifications(stat, stage, move, target, isUser, floor, events);
	}

}

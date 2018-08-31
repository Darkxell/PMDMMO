package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class AbilityStatBoostWhileAfflicted extends Ability
{
	public final Stat stat;

	public AbilityStatBoostWhileAfflicted(int id, Stat stat)
	{
		super(id);
		this.stat = stat;
	}

	@Override
	public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (isUser && stat == Stat.Defense || stat == Stat.SpecialDefense || stat == Stat.Evasiveness)
			return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
		if (!isUser && stat == Stat.Attack || stat == Stat.SpecialAttack || stat == Stat.Accuracy)
			return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
		if (stat == this.stat && ((isUser && this.isAfflicted(move.user)) || (!isUser && this.isAfflicted(target))))
		{
			events.add(new TriggeredAbilityEvent(floor, isUser ? move.user : target));
			return value * 2;
		}
		return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
	}

	private boolean isAfflicted(DungeonPokemon target)
	{
		return target.hasStatusCondition(StatusCondition.Poisoned) || target.hasStatusCondition(StatusCondition.Burn)
				|| target.hasStatusCondition(StatusCondition.Paralyzed);
	}

}

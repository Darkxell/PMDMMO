package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class AbilityStatBoostWithAlly extends Ability
{

	Ability allyAbility;
	public final double multiplier;
	public final Stat stat;

	public AbilityStatBoostWithAlly(int id, Stat stat, double multiplier)
	{
		super(id);
		this.stat = stat;
		this.multiplier = multiplier;
	}

	@Override
	public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (stat == this.stat && isUser)
		{
			boolean found = false;
			for (DungeonPokemon p : floor.listPokemon())
				if (p.ability() == this.allyAbility && p.isAlliedWith(p))
				{
					found = true;
					break;
				}
			if (found)
			{
				events.add(new TriggeredAbilityEvent(floor, move.user));
				return value * this.multiplier;
			}
		}
		return super.applyStatModifications(stat, value, move, target, isUser, floor, events);
	}

}

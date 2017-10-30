package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;

public class PeriodicDamageStatusCondition extends StatusCondition implements DamageSource
{
	/** The damage this Status Condition deals. */
	public final int damage;
	/** The number of turns between each damage dealt. */
	public final int period;

	public PeriodicDamageStatusCondition(int id, int durationMin, int durationMax, int period, int damage)
	{
		super(id, durationMin, durationMax);
		this.period = period;
		this.damage = damage;
	}

	@Override
	public ArrayList<DungeonEvent> tick(Floor floor, StatusConditionInstance instance)
	{
		ArrayList<DungeonEvent> events = super.tick(floor, instance);
		if (instance.tick % this.period == 0) events.add(new DamageDealtEvent(floor, instance.pokemon, this, this.damage));
		return events;
	}

}

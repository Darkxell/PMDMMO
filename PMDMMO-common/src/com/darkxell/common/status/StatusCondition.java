package com.darkxell.common.status;

import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StatusCondition implements AffectsPokemon
{
	private static final HashMap<Integer, StatusCondition> _registry = new HashMap<Integer, StatusCondition>();

	public static final PreventsActionStatusCondition Asleep = new PreventsActionStatusCondition(3, 3, 6);
	public static final PeriodicDamageStatusCondition Badly_poisoned = new PeriodicDamageStatusCondition(1, -1, -1, 2, 6);
	public static final PeriodicDamageStatusCondition Burn = new PeriodicDamageStatusCondition(2, -1, -1, 20, 5);
	public static final PeriodicDamageStatusCondition Poisoned = new PeriodicDamageStatusCondition(0, -1, -1, 10, 4);

	/** @return The Status Condition with the input ID. */
	public static StatusCondition find(int id)
	{
		return _registry.get(id);
	}

	/** This Status condition's duration. -1 for indefinite. */
	public final int durationMin, durationMax;
	/** This Status Condition's ID. */
	public final int id;

	public StatusCondition(int id, int durationMin, int durationMax)
	{
		this.id = id;
		this.durationMin = durationMin;
		this.durationMax = durationMax;
		_registry.put(this.id, this);
	}

	/** @return True if this Status Condition affects the input Pokemon. */
	public boolean affects(DungeonPokemon pokemon)
	{
		return !pokemon.hasStatusCondition(this);
	}

	public void tick(Floor floor, StatusConditionInstance instance, ArrayList<DungeonEvent> events)
	{}

}

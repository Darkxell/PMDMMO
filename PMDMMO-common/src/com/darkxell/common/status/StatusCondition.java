package com.darkxell.common.status;

import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StatusCondition
{
	private static final HashMap<Integer, StatusCondition> _registry = new HashMap<Integer, StatusCondition>();

	public static final PeriodicDamageStatusCondition POISONED = new PeriodicDamageStatusCondition(0, -1, -1, 10, 4);
	public static final PeriodicDamageStatusCondition BADLY_POISONED = new PeriodicDamageStatusCondition(1, -1, -1, 2, 6);
	public static final PeriodicDamageStatusCondition BURN = new PeriodicDamageStatusCondition(2, -1, -1, 20, 5);

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

	/** @return True if this Status Condition affects the input Pokémon. */
	public boolean affects(DungeonPokemon pokemon)
	{
		return !pokemon.hasStatusCondition(this);
	}

	public ArrayList<DungeonEvent> tick(Floor floor, StatusConditionInstance instance)
	{
		return new ArrayList<DungeonEvent>();
	}

}

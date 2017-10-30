package com.darkxell.common.status;

import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;

public class StatusCondition
{
	private static final HashMap<Integer, StatusCondition> _registry = new HashMap<Integer, StatusCondition>();

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

	public ArrayList<DungeonEvent> tick(Floor floor, StatusConditionInstance instance)
	{
		return new ArrayList<DungeonEvent>();
	}

}

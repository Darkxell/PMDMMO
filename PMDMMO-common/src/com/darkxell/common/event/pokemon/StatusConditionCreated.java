package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.StatusConditionInstance;

public class StatusConditionCreated extends DungeonEvent
{
	public final StatusConditionInstance condition;

	public StatusConditionCreated(Floor floor, StatusConditionInstance condition)
	{
		super(floor);
		this.condition = condition;

		this.messages.add(this.condition.startMessage());
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.condition.pokemon.inflictStatusCondition(this.condition);
		return super.processServer();
	}

}

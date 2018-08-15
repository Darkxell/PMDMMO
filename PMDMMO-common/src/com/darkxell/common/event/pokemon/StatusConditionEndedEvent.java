package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;

public class StatusConditionEndedEvent extends DungeonEvent
{
	public final AppliedStatusCondition condition;

	public StatusConditionEndedEvent(Floor floor, AppliedStatusCondition condition)
	{
		super(floor);
		this.condition = condition;
	}

	@Override
	public String loggerMessage()
	{
		return this.messages.get(0).toString();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.condition.pokemon.removeStatusCondition(this.condition)) this.messages.add(this.condition.endMessage());
		return super.processServer();
	}

}

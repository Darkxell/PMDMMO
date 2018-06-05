package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.StatusConditionInstance;

public class StatusConditionCreatedEvent extends DungeonEvent
{
	public final StatusConditionInstance condition;

	public StatusConditionCreatedEvent(Floor floor, StatusConditionInstance condition)
	{
		super(floor);
		this.condition = condition;
	}

	@Override
	public boolean isValid()
	{
		return !this.condition.pokemon.isFainted();
	}

	@Override
	public String loggerMessage()
	{
		return this.messages.get(0).toString();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages.add(this.condition.startMessage());

		this.condition.pokemon.inflictStatusCondition(this.condition);
		return super.processServer();
	}

}

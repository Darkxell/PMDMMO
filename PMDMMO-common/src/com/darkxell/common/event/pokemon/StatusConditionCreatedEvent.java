package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class StatusConditionCreatedEvent extends DungeonEvent
{
	public final AppliedStatusCondition condition;
	private boolean succeeded = false;

	public StatusConditionCreatedEvent(Floor floor, AppliedStatusCondition condition)
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
		Pair<Boolean, Message> affects = this.condition.condition.affects(this.condition.pokemon);
		this.succeeded = affects.first;
		if (this.succeeded)
		{
			this.messages.add(this.condition.startMessage());
			this.condition.pokemon.inflictStatusCondition(this.condition);
		} else if (affects.second != null) this.messages.add(affects.second);
		return super.processServer();
	}

	/** @return <code>true</code> if the Status Condition was successfully applied. */
	public boolean succeeded()
	{
		return this.succeeded;
	}

}

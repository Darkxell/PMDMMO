package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.language.Message;

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
		return this.condition.condition.name() + " finished for " + this.condition.pokemon.getNickname();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.condition.pokemon.removeStatusCondition(this.condition))
		{
			Message m = this.condition.endMessage();
			if (m != null) this.messages.add(m);
		}
		return super.processServer();
	}

}

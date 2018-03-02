package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.TurnSkippedEvent;

public class AIStateTurnSkipper extends AIState
{

	public AIStateTurnSkipper(AI ai)
	{
		super(ai);
	}

	@Override
	public DungeonEvent takeAction()
	{
		return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
	}
	
	@Override
	public String toString()
	{
		return "Skips turns";
	}

}

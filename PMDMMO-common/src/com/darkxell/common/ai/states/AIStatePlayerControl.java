package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;

public class AIStatePlayerControl extends AIState
{

	public AIStatePlayerControl(AI ai)
	{
		super(ai);
	}

	@Override
	public DungeonEvent takeAction()
	{
		return null;
	}

}

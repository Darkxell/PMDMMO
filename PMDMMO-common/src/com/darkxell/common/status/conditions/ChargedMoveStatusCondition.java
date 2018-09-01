package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.states.AIStateChargedAttack;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;

public class ChargedMoveStatusCondition extends ChangeAIStatusCondition
{

	public final int moveID;

	public ChargedMoveStatusCondition(int id, int durationMin, int durationMax, int moveID)
	{
		super(id, durationMin, durationMax);
		this.moveID = moveID;
	}

	@Override
	public void onEnd(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.onEnd(floor, instance, events);
		floor.aiManager.getAI(instance.pokemon).setSuperState(null);
	}

	@Override
	public void onStart(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.onStart(floor, instance, events);
		AI ai = floor.aiManager.getAI(instance.pokemon);
		ai.setSuperState(new AIStateChargedAttack(ai, this.moveID));
	}

}

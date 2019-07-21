package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.states.AIStateChargedAttack;
import com.darkxell.common.ai.states.AIStateTurnSkipper;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.status.AppliedStatusCondition;

public class ChargedMoveStatusCondition extends ChangeAIStatusCondition
{

	public final int moveID;

	public ChargedMoveStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int moveID)
	{
		super(id, isAilment, durationMin, durationMax);
		this.moveID = moveID;
	}

	@Override
	public void onEnd(Floor floor, AppliedStatusCondition instance, StatusConditionEndReason reason, ArrayList<DungeonEvent> events)
	{
		super.onEnd(floor, instance, reason, events);
		floor.aiManager.getAI(instance.pokemon).setSuperState(null);
	}

	@Override
	public void onStart(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.onStart(floor, instance, events);
		AI ai = floor.aiManager.getAI(instance.pokemon);
		if (instance.duration == 1) ai.setSuperState(new AIStateChargedAttack(ai, this.moveID));
		else ai.setSuperState(new AIStateTurnSkipper(ai));
	}

	@Override
	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.tick(floor, instance, events);
		if (instance.tick == instance.duration - 2)
		{
			AI ai = floor.aiManager.getAI(instance.pokemon);
			ai.setSuperState(new AIStateChargedAttack(ai, this.moveID));
		}
	}

}

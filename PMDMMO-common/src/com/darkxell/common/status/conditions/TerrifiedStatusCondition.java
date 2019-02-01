package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.states.AIStateRunaway;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;

public class TerrifiedStatusCondition extends StatusCondition
{

	public TerrifiedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax)
	{
		super(id, isAilment, durationMin, durationMax);
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
		ai.setSuperState(new AIStateRunaway(ai));
	}

	private boolean souldStatusContinue(DungeonPokemon pokemon)
	{
		if (pokemon.ability() == Ability.RUNAWAY && pokemon.getHpPercentage() < 50) return true;
		return false;
	}

	@Override
	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.tick(floor, instance, events);
		if (instance.tick == instance.duration - 1 && this.souldStatusContinue(instance.pokemon)) --instance.tick;
	}

}

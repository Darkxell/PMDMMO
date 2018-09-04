package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;

public class StoreDamageToDoubleStatusCondition extends ChargedMoveStatusCondition
{

	public StoreDamageToDoubleStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int moveID)
	{
		super(id, isAilment, durationMin, durationMax, moveID);
	}

	@Override
	public void onPostEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onPostEvent(floor, event, concerned, resultingEvents);
		if (event instanceof DamageDealtEvent)
		{
			DamageDealtEvent e = (DamageDealtEvent) event;
			if (e.target.hasStatusCondition(this))
				e.target.getStatusCondition(this).addFlag("damage" + e.target.getStatusCondition(this).listFlags().length + ":" + e.damage);
		}
	}

	@Override
	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.tick(floor, instance, events);
		if (instance.tick == instance.duration - 1 && !instance.hasFlag("attacked")) --instance.tick;
	}

}

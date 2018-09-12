package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusConditions;

public class AbilityRunaway extends Ability
{

	public AbilityRunaway(int id)
	{
		super(id);
	}

	@Override
	public void onPostEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onPostEvent(floor, event, concerned, resultingEvents);
		if (event instanceof DamageDealtEvent)
		{
			DungeonPokemon p = ((DamageDealtEvent) event).target;
			if (p != concerned) return;
			if (p.ability() == this && p.getHpPercentage() < 50 && !p.hasStatusCondition(StatusConditions.Terrified) && !p.isTeamLeader())
				resultingEvents.add(new StatusConditionCreatedEvent(floor, StatusConditions.Terrified.create(p, this, floor.random)));
		}
	}

}

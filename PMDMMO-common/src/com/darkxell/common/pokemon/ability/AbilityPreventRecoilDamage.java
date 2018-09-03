package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventRecoilDamage extends Ability
{

	public AbilityPreventRecoilDamage(int id)
	{
		super(id);
	}

	@Override
	public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onPreEvent(floor, event, concerned, resultingEvents);
		if (event instanceof DamageDealtEvent)
		{
			DamageDealtEvent e = (DamageDealtEvent) event;
			if (e.isRecoilDamage() && e.target.ability() == this) e.consume();
		}
	}

}

package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class MoveDrain extends Move
{

	public MoveDrain(Element xml)
	{
		super(xml);
	}

	public MoveDrain(int id, PokemonType type, MoveCategory category, int pp, int power, int accuracy, MoveRange range, MoveTarget targets, int priority,
			int additionalEffectChance, boolean makesContact)
	{
		super(id, type, -1, category, pp, power, accuracy, range, targets, priority, additionalEffectChance, makesContact);
	}

	@Override
	public void addAdditionalEffects(DungeonPokemon user, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		int health = -1;
		for (DungeonEvent e : events)
			if (e instanceof DamageDealtEvent)
			{
				if (health == -1) ++health;
				health += ((DamageDealtEvent) e).damage / 2;
			}
		
		if (health!=-1) events.add(new HealthRestoredEvent(floor, user, health));
	}

	@Override
	public boolean additionalEffectLands(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		return true;
	}

}

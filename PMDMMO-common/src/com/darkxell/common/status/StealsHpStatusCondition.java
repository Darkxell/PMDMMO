package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class StealsHpStatusCondition extends StatusCondition
{

	/** Amount of HP stolen. */
	public final int hp;
	/** Number of turns after which the HP is stolen. */
	public final int turnCycle;

	public StealsHpStatusCondition(int id, int duration, int turnCycle, int hp)
	{
		super(id, duration, duration);
		this.turnCycle = turnCycle;
		this.hp = hp;
	}

	@Override
	public boolean affects(DungeonPokemon pokemon)
	{
		if (!super.affects(pokemon)) return false;
		if (this == Leech_seed) return !pokemon.species().isType(PokemonType.Grass);
		return true;
	}

	@Override
	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		super.tick(floor, instance, events);
		if (!(instance.source instanceof DungeonPokemon) || ((DungeonPokemon) instance.source).isFainted()) instance.finish(floor, events);
		else if (instance.tick % this.turnCycle == 0)
		{
			events.add(new DamageDealtEvent(floor, instance.pokemon, this, this.hp));
			events.add(new HealthRestoredEvent(floor, (DungeonPokemon) instance.source, this.hp));
		}
	}

}

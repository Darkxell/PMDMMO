package com.darkxell.common.event;

import com.darkxell.common.pokemon.DungeonPokemon;

public class DamageDealtEvent extends DungeonEvent
{

	public final int damage;
	public final DungeonPokemon target;

	public DamageDealtEvent(DungeonPokemon target, int damage)
	{
		this.target = target;
		this.damage = damage;
	}

}

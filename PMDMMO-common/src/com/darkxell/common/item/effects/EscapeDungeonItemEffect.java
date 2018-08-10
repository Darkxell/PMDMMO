package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that has different effects when used. */
public class EscapeDungeonItemEffect extends OrbItemEffect
{

	public EscapeDungeonItemEffect(int id)
	{
		super(id);
	}

	@Override
	public void orbEffect(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		// if (pokemon.player() == null) events.addAll(new PokemonDespawnedEvent(floor, pokemon));else TODO PokemonDespawn on EscapeOrb with no Player
		events.add(new DungeonExitEvent(floor, pokemon.player()));
	}
}

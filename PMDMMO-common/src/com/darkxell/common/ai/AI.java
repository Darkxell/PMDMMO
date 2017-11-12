package com.darkxell.common.ai;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.TurnSkippedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AI
{

	public static DungeonEvent takeAction(Floor floor, DungeonPokemon pokemon)
	{
		if (pokemon == null) return null;
		// if (pokemon.pokemon.player != null) return new PokemonTravelEvent(floor, pokemon, GameUtil.randomDirection(floor.random));
		return new TurnSkippedEvent(floor, pokemon);
	}

}

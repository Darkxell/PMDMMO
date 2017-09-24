package com.darkxell.common.event;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class TurnSkippedEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;

	public TurnSkippedEvent(Floor floor, DungeonPokemon pokemon)
	{
		super(floor);
		this.pokemon = pokemon;
	}

}

package com.darkxell.common.event.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DungeonExitEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;

	public DungeonExitEvent(Floor floor, DungeonPokemon pokemon)
	{
		super(floor);
		this.pokemon = pokemon;
	}

}

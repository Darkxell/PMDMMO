package com.darkxell.common.event.pokemon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** When processed, the Event Processor detects that this Event's Pokémon can't keep running. */
public class CancelRunningEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;

	public CancelRunningEvent(Floor floor, DungeonPokemon pokemon)
	{
		super(floor);
		this.pokemon = pokemon;
	}

}

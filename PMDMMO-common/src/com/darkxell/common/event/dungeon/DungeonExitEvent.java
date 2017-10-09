package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

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
	
	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.pokemon.dispose();
		return super.processServer();
	}

}

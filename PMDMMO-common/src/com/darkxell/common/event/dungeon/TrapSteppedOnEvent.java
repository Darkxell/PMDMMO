package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.trap.Trap;

public class TrapSteppedOnEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;
	public final Tile tile;
	public final Trap trap;

	public TrapSteppedOnEvent(DungeonPokemon pokemon, Tile tile, Trap trap)
	{
		this.pokemon = pokemon;
		this.tile = tile;
		this.trap = trap;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.tile.trapRevealed = true;
		this.resultingEvents.addAll(this.trap.onPokemonStep(this.pokemon));
		return super.processServer();
	}

}

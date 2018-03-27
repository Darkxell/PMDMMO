package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class PokemonSpawnedEvent extends DungeonEvent
{

	/** The spawned Pokémon. */
	public final DungeonPokemon spawned;
	/** The Tile to spawn the Pokémon on. */
	public final Tile tile;

	public PokemonSpawnedEvent(Floor floor, DungeonPokemon spawned, Tile tile)
	{
		super(floor, null);
		this.spawned = spawned;
		this.tile = tile;
	}

	@Override
	public String loggerMessage()
	{
		return this.spawned + " spawned at " + this.tile.x + "," + this.tile.y;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.resultingEvents.addAll(this.floor.summonPokemon(this.spawned, this.tile.x, this.tile.y));
		return super.processServer();
	}

}

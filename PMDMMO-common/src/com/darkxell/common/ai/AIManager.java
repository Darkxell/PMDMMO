package com.darkxell.common.ai;

import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AIManager
{

	private final HashMap<DungeonPokemon, AI> ais;
	final Floor floor;

	public AIManager(Floor floor)
	{
		this.floor = floor;
		this.ais = new HashMap<>();
	}

	/** @return The AI the input Pokémon should have. */
	private AI aiFor(DungeonPokemon pokemon)
	{
		if (pokemon.player() == null) return new WildAI(this.floor, pokemon);
		else return new AllyAI(this.floor, pokemon, pokemon.player().getDungeonLeader());
		// return new SkipTurnsAI(this.floor, pokemon);
	}

	public AI getAI(DungeonPokemon pokemon)
	{
		return this.ais.get(pokemon);
	}

	public void register(DungeonPokemon pokemon)
	{
		if (!pokemon.isTeamLeader()) this.ais.put(pokemon, this.aiFor(pokemon));
	}

	public DungeonEvent takeAction(DungeonPokemon pokemon)
	{
		if (pokemon == null || !this.ais.containsKey(pokemon)) return null;
		return this.ais.get(pokemon).takeAction();
	}

	public void unregister(DungeonPokemon pokemon)
	{
		this.ais.remove(pokemon);
	}

}

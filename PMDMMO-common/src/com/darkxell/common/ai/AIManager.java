package com.darkxell.common.ai;

import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Logger;

public class AIManager
{

	private final HashMap<DungeonPokemon, AI> ais;
	final Floor floor;

	public AIManager(Floor floor)
	{
		this.floor = floor;
		this.ais = new HashMap<>();
	}

	/** @return The AI the input Pok�mon should have. */
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
		this.register(pokemon, null);
	}

	public void register(DungeonPokemon pokemon, AI ai)
	{
		if (ai == null && !pokemon.isTeamLeader()) ai = this.aiFor(pokemon);
		if (ai != null) this.ais.put(pokemon, ai);
	}

	public DungeonEvent takeAction(DungeonPokemon pokemon)
	{
		if (pokemon == null) return null;
		if (!pokemon.canAct(this.floor)) return new TurnSkippedEvent(this.floor, pokemon);
		if (!this.ais.containsKey(pokemon))
		{
			Logger.e("Tried to call " + pokemon + "'s AI even though it has none.");
			return null;
		}
		return this.ais.get(pokemon).takeAction();
	}

	public void unregister(DungeonPokemon pokemon)
	{
		if (!this.ais.containsKey(pokemon))
		{
			Logger.e(pokemon + " already has no AI: can't unregister!");
			return;
		}
		this.ais.remove(pokemon);
	}

}

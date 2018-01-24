package com.darkxell.common.ai;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.PokemonRotateEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AllyAI extends AI
{

	public final DungeonPokemon leader;

	public AllyAI(Floor floor, DungeonPokemon pokemon, DungeonPokemon leader)
	{
		super(floor, pokemon);
		this.leader = leader;
	}

	@Override
	public DungeonEvent takeAction()
	{
		short direction = AIUtils.generalDirection(this.pokemon, this.leader);
		if (AIUtils.isAdjacentTo(this.pokemon, this.leader, false)) return new PokemonRotateEvent(this.floor, this.pokemon, direction, true);
		return new PokemonTravelEvent(this.floor, this.pokemon, direction);
	}

}

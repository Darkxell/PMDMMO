package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.PokemonRotateEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AIStateFollowPokemon extends AIState
{

	public final DungeonPokemon target;

	public AIStateFollowPokemon(AI ai, DungeonPokemon target)
	{
		super(ai);
		this.target = target;
	}

	@Override
	public DungeonEvent takeAction()
	{
		short direction = AIUtils.generalDirection(this.ai.pokemon, this.target);
		if (AIUtils.isAdjacentTo(this.ai.pokemon, this.target, true)) return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction, true);

		short go = AIUtils.direction(this.ai.floor, this.ai.pokemon, this.target);
		if (go == -1) return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction, true);
		return new PokemonTravelEvent(this.ai.floor, this.ai.pokemon, go);
	}

}

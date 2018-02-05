package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.PokemonRotateEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Direction;

public class AIStateFollowPokemon extends AIState
{

	/** The Tile the target was last seen at. */
	protected Tile lastSeen;
	public final DungeonPokemon target;

	public AIStateFollowPokemon(AI ai, DungeonPokemon target)
	{
		super(ai);
		this.target = target;
	}

	public Tile lastSeen()
	{
		return this.lastSeen;
	}

	@Override
	public DungeonEvent takeAction()
	{
		this.lastSeen = this.target.tile();

		Direction direction = AIUtils.generalDirection(this.ai.pokemon, this.target);
		if (AIUtils.isAdjacentTo(this.ai.pokemon, this.target, true))
		{
			Direction d = AIUtils.adjacentEnemyDirection(this.ai.floor, this.ai.pokemon);
			if (d != null)
			{
				LearnedMove move = this.ai.pokemon.pokemon.move(this.ai.floor.random.nextInt(this.ai.pokemon.pokemon.moveCount()));
				return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon, d);
			}
			return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction, true);
		}

		Direction go = AIUtils.direction(this.ai.pokemon, this.target);
		if (go == null) return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction, true);
		return new PokemonTravelEvent(this.ai.floor, this.ai.pokemon, go);
	}

}

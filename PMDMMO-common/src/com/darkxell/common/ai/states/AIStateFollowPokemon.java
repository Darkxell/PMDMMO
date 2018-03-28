package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.MoveRegistry;
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
	public Direction mayRotate()
	{
		return AIUtils.generalDirection(this.ai.pokemon, this.target);
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
				int moveIndex = this.ai.floor.random.nextInt(this.ai.pokemon.moveCount() + 1);
				LearnedMove move = moveIndex == this.ai.pokemon.moveCount() ? new LearnedMove(MoveRegistry.ATTACK.id) : this.ai.pokemon.move(moveIndex);
				return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon, d);
			}
			if (direction != this.ai.pokemon.facing()) return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction);
			return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
		}

		Direction go = AIUtils.direction(this.ai.pokemon, this.target);
		if (go == null)
		{
			if (direction != this.ai.pokemon.facing()) return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction);
			return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
		}
		return new PokemonTravelEvent(this.ai.floor, this.ai.pokemon, go);
	}

	@Override
	public String toString()
	{
		return "Follows " + this.target;
	}

}

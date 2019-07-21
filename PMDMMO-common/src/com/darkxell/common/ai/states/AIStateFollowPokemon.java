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
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Direction;

/** State in which the Pokemon follows a Pokemon. */
public class AIStateFollowPokemon extends AIState {

	/** The Tile the target was last seen at. */
	protected Tile lastSeen;
	/** The Pokemon to follow. */
	public final DungeonPokemon target;

	public AIStateFollowPokemon(AI ai, DungeonPokemon target) {
		super(ai);
		this.target = target;
	}

	public Tile lastSeen() {
		return this.lastSeen;
	}

	@Override
	public Direction mayRotate() {
		if (this.target.isFainted()) return null;
		return AIUtils.generalDirection(this.ai.pokemon, this.target);
	}

	@Override
	public DungeonEvent takeAction() {
		// If can see the target, update the last seen position.
		if (AIUtils.isVisible(this.ai.floor, this.ai.pokemon, this.target)) this.lastSeen = this.target.tile();

		Direction direction = AIUtils.generalDirection(this.ai.pokemon, this.target);

		// If adjacent to target, fight enemies
		if (AIUtils.isAdjacentTo(this.ai.pokemon, this.target, true)) {
			Direction d = AIUtils.adjacentEnemyDirection(this.ai.floor, this.ai.pokemon);
			if (d != null && this.ai.pokemon.canAttack(this.ai.floor)) {
				LearnedMove move = AIUtils.chooseMove(this.ai);
				return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon, d);
			}
			if (direction != this.ai.pokemon.facing())
				return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction);
			return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
		}

		// Else try to reach the target
		Direction go = AIUtils.direction(this.ai.pokemon, this.target);
		if (go == null || !this.ai.pokemon.canMove(this.ai.floor)) {
			if (direction != this.ai.pokemon.facing())
				return new PokemonRotateEvent(this.ai.floor, this.ai.pokemon, direction);
			return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
		}
		return new PokemonTravelEvent(this.ai.floor, this.ai.pokemon, go);
	}

	@Override
	public String toString() {
		return "Follows " + this.target;
	}

}

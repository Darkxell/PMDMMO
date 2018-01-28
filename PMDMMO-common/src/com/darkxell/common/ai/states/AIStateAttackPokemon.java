package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class AIStateAttackPokemon extends AIStateFollowPokemon
{

	public AIStateAttackPokemon(AI ai, DungeonPokemon target)
	{
		super(ai, target);
	}

	@Override
	public DungeonEvent takeAction()
	{
		if (AIUtils.isAdjacentTo(this.ai.pokemon, this.target, true))
		{
			LearnedMove move = this.ai.pokemon.pokemon.move(this.ai.floor.random.nextInt(this.ai.pokemon.pokemon.moveCount()));
			return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon, AIUtils.generalDirection(this.ai.pokemon, this.target));
		}
		return super.takeAction();
	}

}

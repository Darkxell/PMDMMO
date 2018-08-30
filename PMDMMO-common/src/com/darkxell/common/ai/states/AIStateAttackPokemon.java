package com.darkxell.common.ai.states;

import java.util.ArrayList;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.RandomUtil;

/** State in which the Pokemon follows then attacks a Pokemon. */
public class AIStateAttackPokemon extends AIStateFollowPokemon
{

	public AIStateAttackPokemon(AI ai, DungeonPokemon target)
	{
		super(ai, target);
	}

	@Override
	public DungeonEvent takeAction()
	{
		// Only attack if adjacent. TODO Change Attack AI to take Move ranges into account.
		if (AIUtils.isAdjacentTo(this.ai.pokemon, this.target, true) && this.ai.pokemon.canAttack(this.ai.floor))
		{
			// Choose a move at random.
			ArrayList<LearnedMove> candidates = new ArrayList<>();
			for (int i = 0; i < this.ai.pokemon.moveCount(); ++i)
				candidates.add(this.ai.pokemon.move(i));
			candidates.removeIf(m -> m.pp() == 0);
			if (candidates.isEmpty()) candidates.add(new LearnedMove(MoveRegistry.STRUGGLE.id));
			candidates.add(new LearnedMove(MoveRegistry.ATTACK.id));
			LearnedMove move = RandomUtil.random(candidates, this.ai.floor.random);
			return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon, AIUtils.generalDirection(this.ai.pokemon, this.target));
		}
		return super.takeAction();
	}

	@Override
	public String toString()
	{
		return "Attacks " + this.target;
	}

}

package com.darkxell.common.ai;

import java.util.ArrayList;

import com.darkxell.common.ai.states.AIStateFollowPokemon;
import com.darkxell.common.ai.states.AIStateTurnSkipper;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class WildAI extends AI
{

	public WildAI(Floor floor, DungeonPokemon pokemon)
	{
		super(floor, pokemon);
	}

	@Override
	protected void update()
	{
		if (this.state instanceof AIStateFollowPokemon)
		{
			DungeonPokemon target = ((AIStateFollowPokemon) this.state).target;
			if (!AIUtils.isVisible(this.floor, this.pokemon, target)) this.state = new AIStateTurnSkipper(this);
		} else if (AIUtils.hasVisibleEnemies(this.floor, this.pokemon))
		{
			ArrayList<DungeonPokemon> candidates = AIUtils.visibleEnemies(this.floor, this.pokemon);
			this.state = new AIStateFollowPokemon(this, candidates.get(0));
		}
	}

}

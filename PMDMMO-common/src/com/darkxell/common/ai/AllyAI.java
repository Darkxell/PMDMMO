package com.darkxell.common.ai;

import com.darkxell.common.ai.states.AIStateExplore;
import com.darkxell.common.ai.states.AIStateFollowPokemon;
import com.darkxell.common.dungeon.floor.Floor;
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
	protected void update()
	{
		if (AIUtils.isVisible(this.floor, this.pokemon, this.leader))
		{
			if (!(this.state instanceof AIStateFollowPokemon)) this.state = new AIStateFollowPokemon(this, this.leader);
		} else if (this.state instanceof AIStateFollowPokemon) this.state = new AIStateExplore(this, ((AIStateFollowPokemon) this.state).lastSeen());
	}

}

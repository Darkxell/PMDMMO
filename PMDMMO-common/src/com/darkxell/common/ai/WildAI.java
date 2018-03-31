package com.darkxell.common.ai;

import java.util.ArrayList;

import com.darkxell.common.ai.states.AIStateAttackPokemon;
import com.darkxell.common.ai.states.AIStateExplore;
import com.darkxell.common.ai.states.AIStateFollowPokemon;
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
		ArrayList<DungeonPokemon> enemies = AIUtils.visibleEnemies(this.floor, this.pokemon);
		if (!enemies.isEmpty()) this.state = new AIStateAttackPokemon(this, enemies.get(0));
		else if (this.state instanceof AIStateFollowPokemon) this.state = new AIStateExplore(this, ((AIStateFollowPokemon) this.state).lastSeen());
		else if (!(this.state instanceof AIStateExplore)) this.state = new AIStateExplore(this);
	}

}

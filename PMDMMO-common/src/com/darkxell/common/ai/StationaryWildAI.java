package com.darkxell.common.ai;

import com.darkxell.common.ai.states.AIStateExplore;
import com.darkxell.common.ai.states.AIStateFollowPokemon;
import com.darkxell.common.ai.states.AIStateTurnSkipper;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StationaryWildAI extends WildAI {

	public StationaryWildAI(Floor floor, DungeonPokemon pokemon) {
		super(floor, pokemon);
	}

	@Override
	public AIState defaultState() {
		return new AIStateTurnSkipper(this);
	}

	@Override
	protected void update() {
		super.update();

		if (this.state instanceof AIStateExplore) this.state = new AIStateTurnSkipper(this);
		else if (this.state instanceof AIStateFollowPokemon) ((AIStateFollowPokemon) this.state).canMove = false;
	}

}

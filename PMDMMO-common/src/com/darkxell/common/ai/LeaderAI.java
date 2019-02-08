package com.darkxell.common.ai;

import com.darkxell.common.ai.states.AIStatePlayerControl;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class LeaderAI extends AI {

    public LeaderAI(Floor floor, DungeonPokemon pokemon) {
        super(floor, pokemon);
    }

    @Override
    public AIState defaultState() {
        return new AIStatePlayerControl(this);
    }

    @Override
    protected void update() {
    }

}

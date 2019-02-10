package com.darkxell.common.ai;

import com.darkxell.common.ai.states.AIStateExplore;
import com.darkxell.common.ai.states.AIStateFollowPokemon;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

/** AI of a Rescue Team member. */
public class AllyAI extends AI {

    /** Reference to the leader. */
    public final DungeonPokemon leader;

    public AllyAI(Floor floor, DungeonPokemon pokemon, DungeonPokemon leader) {
        super(floor, pokemon);
        this.leader = leader;
    }

    @Override
    public AIState defaultState() {
        return new AIStateExplore(this, null);
    }

    @Override
    protected void update() {
        if (this.visibility.isVisible(this.leader)) {
            if (!(this.state instanceof AIStateFollowPokemon))
                this.state = new AIStateFollowPokemon(this, this.leader);
        } else if (this.state instanceof AIStateFollowPokemon)
            this.state = new AIStateExplore(this, ((AIStateFollowPokemon) this.state).lastSeen());
    }

}

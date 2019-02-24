package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

/** State in which the Pokemon follows then attacks a Pokemon. */
public class AIStateAttackPokemon extends AIStateFollowPokemon {

    public AIStateAttackPokemon(AI ai, DungeonPokemon target) {
        super(ai, target);
    }

    @Override
    public DungeonEvent takeAction() {
        // Only attack if adjacent. TODO Change Attack AI to take Move ranges into account.
        if (AIUtils.isAdjacentTo(this.ai.pokemon, this.target, true) && this.ai.pokemon.canAttack(this.ai.floor)) {
            // Choose a move at random.
            LearnedMove move = AIUtils.chooseMove(this.ai);
            return new MoveSelectionEvent(this.ai.floor, DungeonEventSource.PLAYER_ACTION, move, this.ai.pokemon,
                    AIUtils.generalDirection(this.ai.pokemon, this.target));
        }
        return super.takeAction();
    }

    @Override
    public String toString() {
        return "Attacks " + this.target;
    }

}

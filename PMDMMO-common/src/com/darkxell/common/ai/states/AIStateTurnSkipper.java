package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;

/** State in which the Pokemon skips turns. */
public class AIStateTurnSkipper extends AIState {

    public AIStateTurnSkipper(AI ai) {
        super(ai);
    }

    @Override
    public DungeonEvent takeAction() {
        return new TurnSkippedEvent(this.ai.floor, eventSource, this.ai.pokemon);
    }

    @Override
    public String toString() {
        return "Skips turns";
    }

}

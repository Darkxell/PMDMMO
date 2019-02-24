package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.action.TurnSkippedEvent;

public class AIStatePlayerControl extends AIState {

    public AIStatePlayerControl(AI ai) {
        super(ai);
    }

    @Override
    public DungeonEvent takeAction() {
        return new TurnSkippedEvent(this.ai.floor, DungeonEventSource.PLAYER_ACTION, this.ai.pokemon);
    }

}

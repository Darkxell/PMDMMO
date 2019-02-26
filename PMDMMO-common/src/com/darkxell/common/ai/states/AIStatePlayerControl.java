package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.action.TurnSkippedEvent;

public class AIStatePlayerControl extends AIState {

    public AIStatePlayerControl(AI ai) {
        super(ai);
    }

    @Override
    public Event takeAction() {
        return new TurnSkippedEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, this.ai.pokemon);
    }

}

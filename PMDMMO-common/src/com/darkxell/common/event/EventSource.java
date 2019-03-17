package com.darkxell.common.event;

public interface EventSource {

    public enum BaseEventSource implements EventSource {
        /** Event was created for client purpuses. */
        CLIENT_PURPUSES,
        /** Event was created due to a player or AI taking an action on their turn. */
        PLAYER_ACTION,
        /** Event was created due to an start-of-turn or start-of-floor trigger. */
        TRIGGER;
    }

}

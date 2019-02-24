package com.darkxell.common.event;

public interface DungeonEventSource {

	/** Event was created due to a player or AI taking an action on their turn. */
	public static final DungeonEventSource PLAYER_ACTION = new DungeonEventSource() {};
	/** Event was created due to an start-of-turn or start-of-floor trigger. */
	public static final DungeonEventSource TRIGGER = new DungeonEventSource() {};

}

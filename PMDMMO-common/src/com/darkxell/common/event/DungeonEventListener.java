package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;

public interface DungeonEventListener
{

	/** Method called just after an Event is processed.
	 * 
	 * @param floor - The Floor context.
	 * @param event - The processed Event.
	 * @param resultingEvents - List to store any created Events. They will be added to the list of pending Events. */
	public default void onPostEvent(Floor floor, DungeonEvent event, ArrayList<DungeonEvent> resultingEvents)
	{}

	/** Method called just before an Event is processed.
	 * 
	 * @param floor - The Floor context.
	 * @param event - The processed Event.
	 * @param resultingEvents - List to store any created Events. They will be added to the list of pending Events. */
	public default void onPreEvent(Floor floor, DungeonEvent event, ArrayList<DungeonEvent> resultingEvents)
	{}

}

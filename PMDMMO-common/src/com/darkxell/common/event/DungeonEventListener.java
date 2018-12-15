package com.darkxell.common.event;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

import java.util.ArrayList;

public interface DungeonEventListener
{

	/** Method called just after an Event is processed.
	 * 
	 * @param floor - The Floor context.
	 * @param event - The processed Event.
	 * @param concerned - A reference to a Pokemon for various uses. It is the owner this is an Item, a Status Condition or an Ability.
	 * @param resultingEvents - List to store any created Events. They will be added to the list of pending Events. */
	public default void onPostEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{}

	/** Method called just before an Event is processed.
	 * 
	 * @param floor - The Floor context.
	 * @param event - The processed Event.
	 * @param concerned - A reference to a Pokemon for various uses. It is the owner this is an Item, a Status Condition or an Ability.
	 * @param resultingEvents - List to store any created Events. They will be added to the list of pending Events. */
	public default void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{}

}

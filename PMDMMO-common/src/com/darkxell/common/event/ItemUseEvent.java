package com.darkxell.common.event;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

/** Describes the events occurring while using an Item. */
public class ItemUseEvent
{

	/** The Floor the user is on. */
	public final Floor floor;
	/** The Item that was used. */
	public final Item item;
	/** The messages that were generated. */
	private Message[] messages;
	/** The Pok�mon that the Item was used on. null if there was no target. */
	public final DungeonPokemon target;
	/** The Pok�mon that used the Item. */
	public final DungeonPokemon user;

	public ItemUseEvent(Item item, DungeonPokemon user, DungeonPokemon target, Floor floor, Message... messages)
	{
		this.item = item;
		this.user = user;
		this.target = target;
		this.floor = floor;
		this.messages = messages;
	}

	/** @return The messages that were generated. */
	public Message[] getMessages()
	{
		return messages.clone();
	}

}

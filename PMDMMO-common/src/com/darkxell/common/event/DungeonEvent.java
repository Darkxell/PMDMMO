package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public abstract class DungeonEvent
{

	/** Event that only displays a message. */
	public static class MessageEvent extends DungeonEvent
	{

		public MessageEvent(Floor floor, Message message)
		{
			super(floor);
			this.messages.add(message);
		}

		@Override
		public String loggerMessage()
		{
			return "Showed message: " + this.messages.get(0);
		}
	}

	public static final byte PRIORITY_DEFAULT = 0, PRIORITY_AFTER_MOVE = 1, PRIORITY_ACTION_END = 2, PRIORITY_TURN_END = 3;

	/** The Pokémon that performed the action triggering this Event. This action will consume its turn. May be null if no performer or if this Event doesn't consume the actor's turn. */
	public final DungeonPokemon actor;
	/** The Floor this Event occurs on. */
	public final Floor floor;
	/** The messages that were generated. */
	protected ArrayList<Message> messages;
	/** The priority of this DungeonEvent. */
	protected byte priority;
	/** The events that resulted from this Event. */
	protected ArrayList<DungeonEvent> resultingEvents;

	public DungeonEvent(Floor floor)
	{
		this(floor, null);
	}

	public DungeonEvent(Floor floor, DungeonPokemon actor)
	{
		this.floor = floor;
		this.actor = actor;
		this.priority = PRIORITY_DEFAULT;
		this.messages = new ArrayList<Message>();
		this.resultingEvents = new ArrayList<DungeonEvent>();
	}

	/** @return The messages that were generated. */
	public Message[] getMessages()
	{
		return this.messages.toArray(new Message[this.messages.size()]);
	}

	/** @return The events that resulted from this Event. */
	public DungeonEvent[] getResultingEvents()
	{
		return this.resultingEvents.toArray(new DungeonEvent[this.resultingEvents.size()]);
	}

	/** @return True if this Event should occur. This needs to be checked when called in case other Events on the stack triggered actions that cancel this Event, such as fainting a Pokémon which is this Event's target. */
	public boolean isValid()
	{
		return true;
	}

	public abstract String loggerMessage();

	/** Processes this Event server-side.
	 * 
	 * @return The list of resulting Events. */
	public ArrayList<DungeonEvent> processServer()
	{
		return this.resultingEvents;
	}

}

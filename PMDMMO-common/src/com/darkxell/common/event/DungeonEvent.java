package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.util.Message;

public abstract class DungeonEvent
{

	/** Event that only displays a message. */
	public static class MessageEvent extends DungeonEvent
	{

		public MessageEvent(Message message)
		{
			this.messages.add(message);
		}
	}

	/** The messages that were generated. */
	protected ArrayList<Message> messages;

	public DungeonEvent()
	{
		this.messages = new ArrayList<Message>();
	}

	/** @return The messages that were generated. */
	public Message[] getMessages()
	{
		return this.messages.toArray(new Message[this.messages.size()]);
	}

	/** @return True if this Event should occur. This needs to be checked when called in case other Events on the stack triggered actions that cancel this Event, such as fainting a Pokémon which is this Event's target. */
	public boolean isValid()
	{
		return true;
	}

	/** Processes this Event server-side.
	 * 
	 * @return The list of resulting Events. */
	public DungeonEvent[] processServer()
	{
		return new DungeonEvent[0];
	}

}

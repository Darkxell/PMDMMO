package com.darkxell.common.event;

import com.darkxell.common.util.Message;

public abstract class DungeonEvent
{

	/** Event that only displays messages. */
	public static class MessageEvent extends DungeonEvent
	{
		public MessageEvent(Message... messages)
		{
			super(messages);
		}
	}

	/** The messages that were generated. */
	private final Message[] messages;

	public DungeonEvent(Message... messages)
	{
		this.messages = messages;
	}

	/** @return The messages that were generated. */
	public Message[] getMessages()
	{
		return messages.clone();
	}

	/** Processes this Event server-side.
	 * 
	 * @return The list of resulting Events. */
	public DungeonEvent[] processServer()
	{
		return new DungeonEvent[0];
	}

}

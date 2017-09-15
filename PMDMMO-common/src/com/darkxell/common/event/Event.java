package com.darkxell.common.event;

public abstract class Event
{

	/** The Events created by this Event. */
	private Event[] resultingEvents;

	public Event(Event... resultingEvents)
	{
		this.resultingEvents = resultingEvents;
	}

	/** @return The Events created by this Event. */
	public Event[] getResultingEvents()
	{
		return resultingEvents.clone();
	}

}

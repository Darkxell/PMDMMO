package com.darkxell.common.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

/** Processes game logic events. */
public class CommonEventProcessor
{
	public final DungeonInstance dungeon;
	/** Pending events to process. */
	protected final Stack<DungeonEvent> pending = new Stack<DungeonEvent>();
	/** While processing an event, setting this to false will stop processing the pending events. */
	public boolean processPending = true;

	public CommonEventProcessor(DungeonInstance dungeon)
	{
		this.dungeon = dungeon;
	}

	public void actorTravels(DungeonPokemon actor, short direction, boolean running)
	{
		ArrayList<PokemonTravel> travellers = new ArrayList<PokemonTravel>();
		{
			PokemonTravel t = new PokemonTravel(actor, running, direction);
			travellers.add(t);
			if (t.destination.getPokemon() != null)
			{
				travellers.add(new PokemonTravel(t.destination.getPokemon(), running, Directions.oppositeOf(direction)));
				this.dungeon.takeAction(t.destination.getPokemon());
			}
		}
		this.dungeon.takeAction(actor);
		boolean flag = true;
		DungeonEvent e = null;
		ArrayList<DungeonPokemon> skippers = new ArrayList<DungeonPokemon>();
		while (flag)
		{
			e = this.dungeon.currentFloor().aiManager.takeAction(this.dungeon.nextActor());

			if (e instanceof PokemonTravelEvent)
			{
				PokemonTravel event = ((PokemonTravelEvent) e).getTravel();
				travellers.add(event);
				// Simulating travel
				event.origin.removePokemon(event.pokemon);
				event.destination.setPokemon(event.pokemon);
				this.dungeon.takeAction(event.pokemon);
			} else if (e instanceof TurnSkippedEvent)
			{
				// Simulating skipping
				skippers.add(e.actor);
				this.dungeon.takeAction(e.actor);
				this.addToPending(e);
			} else flag = false;
		}

		// Resetting simulations
		for (PokemonTravel travel : travellers)
			travel.destination.removePokemon(travel.pokemon);
		for (PokemonTravel travel : travellers)
			travel.origin.setPokemon(travel.pokemon);

		// Resetting turns taken
		for (PokemonTravel travel : travellers)
			this.dungeon.resetAction(travel.pokemon);

		PokemonTravelEvent event = new PokemonTravelEvent(this.dungeon.currentFloor(), travellers.toArray(new PokemonTravel[travellers.size()]));
		this.processEvent(event);
	}

	/** Adds the input events to the pending stack, without processing them. */
	public void addToPending(ArrayList<DungeonEvent> arrayList)
	{
		for (int i = arrayList.size() - 1; i >= 0; --i)
			this.addToPending(arrayList.get(i));
	}

	/** Adds the input event to the pending stack, without processing it. */
	public void addToPending(DungeonEvent event)
	{
		if (event != null)
		{
			for (int i = this.pending.size() - 1; i >= 0; --i)
			{
				if (this.pending.get(i).priority >= event.priority)
				{
					this.pending.insertElementAt(event, i + 1);
					return;
				}
			}
			this.pending.insertElementAt(event, 0);
		}
	}

	/** This Event is checked and ready to be processed. */
	protected void doProcess(DungeonEvent event)
	{
		this.addToPending(event.processServer());
	}

	public boolean hasPendingEvents()
	{
		return this.pending.size() != 0;
	}

	protected void onTurnEnd()
	{
		this.addToPending(this.dungeon.endTurn());
		this.processPending();
	}

	/** Processes the input event and adds the resulting events to the pending stack. */
	public void processEvent(DungeonEvent event)
	{
		this.processPending = true;
		this.dungeon.eventOccured(event);
		if (event.actor != null) this.dungeon.takeAction(event.actor);
		else if (event instanceof PokemonTravelEvent) for (PokemonTravel travel : ((PokemonTravelEvent) event).travels())
			this.dungeon.takeAction(travel.pokemon);

		if (event.isValid()) this.doProcess(event);
		if (this.processPending) this.processPending();
	}

	/** Adds all the input events to the pending stack and starts processing them. May not process all events in order if some produce new Events. */
	public void processEvents(ArrayList<DungeonEvent> events)
	{
		this.addToPending(events);
		this.processPending();
	}

	/** Processes the next pending event. */
	public void processPending()
	{
		if (this.hasPendingEvents()) this.processEvent(this.pending.pop());
		else
		{
			DungeonPokemon actor = this.dungeon.nextActor();
			if (actor == null)
			{
				this.onTurnEnd();
				return;
			} else if (actor.isTeamLeader()) return;
			else
			{
				this.addToPending(this.dungeon.currentFloor().aiManager.takeAction(actor));
				this.processPending();
			}
		}
	}

}

package com.darkxell.common.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.ai.AI;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;
import com.darkxell.common.pokemon.DungeonPokemon;

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

	public void actorTravels(short direction, boolean running)
	{
		ArrayList<PokemonTravel> travellers = new ArrayList<PokemonTravel>();
		travellers.add(new PokemonTravel(this.dungeon.getActor(), running, direction));
		boolean flag = true;
		DungeonEvent e = null;
		while (flag)
		{
			e = AI.makeAction(this.dungeon.currentFloor(), this.dungeon.nextActor());

			if (e instanceof PokemonTravelEvent)
			{
				PokemonTravel event = ((PokemonTravelEvent) e).getTravel();
				travellers.add(event);
				// Simulating travel
				event.origin.removePokemon(event.pokemon);
				event.destination.setPokemon(event.pokemon);
			} else if (!(e instanceof TurnSkippedEvent)) flag = false;
		}

		// Resetting simulations
		for (PokemonTravel travel : travellers)
			travel.destination.removePokemon(travel.pokemon);
		for (PokemonTravel travel : travellers)
			travel.origin.setPokemon(travel.pokemon);

		if (e != null) addToPending(e);
		PokemonTravelEvent event = new PokemonTravelEvent(this.dungeon.currentFloor(), travellers.toArray(new PokemonTravel[travellers.size()]));
		processEvent(event);
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
		if (event != null) this.pending.add(event);
	}

	/** This Event is checked and ready to be processed. */
	protected void doProcess(DungeonEvent event)
	{
		addToPending(event.processServer());
	}

	public boolean hasPendingEvents()
	{
		return this.pending.size() != 0;
	}

	protected void onTurnEnd()
	{
		addToPending(this.dungeon.endTurn());
		processPending();
	}

	/** Processes the input event and adds the resulting events to the pending stack. */
	public void processEvent(DungeonEvent event)
	{
		this.processPending = true;
		this.dungeon.eventOccured(event);

		if (event.isValid()) doProcess(event);
		if (this.processPending) processPending();
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
		if (this.hasPendingEvents()) processEvent(this.pending.pop());
		else
		{
			DungeonInstance dungeon = this.dungeon;
			DungeonPokemon actor = dungeon.nextActor();
			if (actor == null)
			{
				this.onTurnEnd();
				return;
			} else if (actor.isTeamLeader()) return;
			else
			{
				addToPending(AI.makeAction(this.dungeon.currentFloor(), actor));
				processPending();
			}
		}
	}

}

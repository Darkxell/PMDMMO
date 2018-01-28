package com.darkxell.common.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.event.pokemon.BellyChangedEvent;
import com.darkxell.common.event.pokemon.PokemonRotateEvent;
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
	/** Lists the Players currently running. */
	private ArrayList<DungeonPokemon> runners = new ArrayList<>();

	public CommonEventProcessor(DungeonInstance dungeon)
	{
		this.dungeon = dungeon;
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

	public void onTurnEnd()
	{
		this.addToPending(this.dungeon.endTurn());
		this.processPending();
	}

	public void pokemonTravels(DungeonPokemon pokemon, short direction, boolean running)
	{
		ArrayList<PokemonTravel> travellers = new ArrayList<PokemonTravel>();

		{ // Applying first travel & checking if switching with ally
			PokemonTravel t = new PokemonTravel(pokemon, running, direction);
			DungeonPokemon switching = t.destination.getPokemon();
			t.origin.removePokemon(pokemon);
			t.destination.setPokemon(pokemon);
			travellers.add(t);
			if (switching != null)
			{
				switching.setTile(t.destination);
				travellers.add(new PokemonTravel(switching, running, Directions.oppositeOf(direction)));
				t.destination.removePokemon(switching);
				t.origin.setPokemon(switching);
				this.dungeon.takeAction(switching);
			}
		}
		this.dungeon.takeAction(pokemon);

		boolean flag = true;
		DungeonEvent e = null;
		ArrayList<DungeonPokemon> skippers = new ArrayList<DungeonPokemon>();
		while (flag)
		{
			DungeonPokemon actor = this.dungeon.nextActor();
			if (actor != null && actor.isTeamLeader())
			{
				if (!this.runners.contains(actor)) e = null;
				else if (this.shouldStopRunning(actor)) e = null;
				else e = new PokemonTravelEvent(this.dungeon.currentFloor(), actor, true, actor.facing());
			} else e = this.dungeon.currentFloor().aiManager.takeAction(actor);

			if (e instanceof PokemonTravelEvent)
			{
				PokemonTravel event = ((PokemonTravelEvent) e).getTravel();
				travellers.add(event);
				// Simulating travel
				event.origin.removePokemon(event.pokemon);
				event.destination.setPokemon(event.pokemon);
				this.dungeon.takeAction(event.pokemon);
			} else if (e instanceof TurnSkippedEvent || e instanceof PokemonRotateEvent)
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

	/** Processes the input event and adds the resulting events to the pending stack. */
	public void processEvent(DungeonEvent event)
	{
		this.processPending = true;
		this.dungeon.eventOccured(event);
		if (event.actor != null) this.dungeon.takeAction(event.actor);
		else if (event instanceof PokemonTravelEvent) for (PokemonTravel travel : ((PokemonTravelEvent) event).travels())
		{
			if (travel.pokemon.isTeamLeader() && travel.running)
			{
				boolean switched = false; // Checking if just switched with ally
				for (PokemonTravel t : ((PokemonTravelEvent) event).travels())
					if (travel != t && travel.isReversed(t))
					{
						switched = true;
						break;
					}

				if (!switched) this.runners.add(travel.pokemon);
			}
			this.dungeon.takeAction(travel.pokemon);
		}

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
			} else if (actor.isTeamLeader())
			{
				if (this.runners.contains(actor))
				{
					if (this.shouldStopRunning(actor))
					{
						this.runners.clear();
						return;
					}
					this.pokemonTravels(actor, actor.facing(), true);
				} else return;
			} else
			{
				DungeonEvent event = this.dungeon.currentFloor().aiManager.takeAction(actor);
				if (event instanceof PokemonTravelEvent)
				{
					PokemonTravelEvent e = (PokemonTravelEvent) event;
					this.pokemonTravels(e.getTravel().pokemon, e.getTravel().direction, e.getTravel().running);
				} else
				{
					this.addToPending(event);
					this.processPending();
				}
			}
		}
	}

	private boolean shouldStopRunning(DungeonPokemon pokemon)
	{
		for (DungeonEvent event : this.pending)
			if (!(event instanceof BellyChangedEvent || event instanceof TurnSkippedEvent || event instanceof PokemonRotateEvent)) return true;
		return AIUtils.shouldStopRunning(pokemon) || this.dungeon.getNextActor() != null;
	}

}

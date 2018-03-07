package com.darkxell.common.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.event.pokemon.BellyChangedEvent;
import com.darkxell.common.event.pokemon.PokemonRotateEvent;
import com.darkxell.common.event.pokemon.PokemonSpawnedEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Processes game logic events. */
public class CommonEventProcessor
{
	public static enum State
	{
		ANIMATING,
		AWATING_INPUT,
		PROCESSING
	}

	public final DungeonInstance dungeon;
	/** Pending events to process. */
	protected final Stack<DungeonEvent> pending = new Stack<DungeonEvent>();
	/** Lists the Players currently running. */
	private ArrayList<DungeonPokemon> runners = new ArrayList<>();
	/** While processing an event, setting this to false will stop processing the pending events. */
	private CommonEventProcessor.State state = CommonEventProcessor.State.AWATING_INPUT;

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
		this.addToPending(this.dungeon.endSubTurn());
		this.processPending();
	}

	/* public void pokemonTravels(DungeonPokemon pokemon, Direction direction, boolean running) { ArrayList<PokemonTravel> travellers = new ArrayList<PokemonTravel>();
	 * 
	 * { // Applying first travel & checking if switching with ally PokemonTravel t = new PokemonTravel(pokemon, running, direction); DungeonPokemon switching = t.destination.getPokemon(); t.origin.removePokemon(pokemon); t.destination.setPokemon(pokemon); travellers.add(t); if (switching != null) {
	 * switching.setTile(t.destination); travellers.add(new PokemonTravel(switching, running, direction.opposite())); t.destination.removePokemon(switching); t.origin.setPokemon(switching); } }
	 * 
	 * boolean flag = true; DungeonEvent e = null; ArrayList<DungeonPokemon> skippers = new ArrayList<DungeonPokemon>(); while (flag) { DungeonPokemon actor = this.dungeon.nextActor(); if (actor == pokemon) break;
	 * 
	 * if (actor != null && actor.isTeamLeader()) { if (!this.runners.contains(actor)) e = null; else if (AIUtils.shouldStopRunning(actor)) e = null; else e = new PokemonTravelEvent(this.dungeon.currentFloor(), actor, true, actor.facing()); } else e =
	 * this.dungeon.currentFloor().aiManager.takeAction(actor);
	 * 
	 * if (e instanceof PokemonTravelEvent) { PokemonTravel event = ((PokemonTravelEvent) e).getTravel(); travellers.add(event);
	 * 
	 * // Simulating travel event.origin.removePokemon(event.pokemon); event.destination.setPokemon(event.pokemon);
	 * 
	 * // Testing if skippers can now move for (int i = 0; i < skippers.size(); ++i) { DungeonEvent s = this.dungeon.currentFloor().aiManager.takeAction(skippers.get(i)); if (s instanceof PokemonTravelEvent) { PokemonTravel se = ((PokemonTravelEvent) s).getTravel(); travellers.add(se);
	 * skippers.remove(i); --i;
	 * 
	 * // Simulating travel se.origin.removePokemon(se.pokemon); se.destination.setPokemon(se.pokemon); } }
	 * 
	 * } else if (e instanceof TurnSkippedEvent || e instanceof PokemonRotateEvent) { // Simulating skipping skippers.add(e.actor); this.addToPending(e); } else flag = false; }
	 * 
	 * // Resetting simulations for (PokemonTravel travel : travellers) travel.destination.removePokemon(travel.pokemon); for (PokemonTravel travel : travellers) travel.origin.setPokemon(travel.pokemon);
	 * 
	 * // Resetting turns taken int total = travellers.size() + skippers.size(); for (int i = 0; i < total; ++i) this.dungeon.previousActor();
	 * 
	 * PokemonTravelEvent event = new PokemonTravelEvent(this.dungeon.currentFloor(), travellers.toArray(new PokemonTravel[travellers.size()])); this.processEvent(event); } */

	/** Processes the input event and adds the resulting events to the pending stack. */
	public void processEvent(DungeonEvent event)
	{
		this.setState(State.PROCESSING);
		this.dungeon.eventOccured(event);
		if (event instanceof PokemonTravelEvent)
		{
			PokemonTravelEvent travel = (PokemonTravelEvent) event;
			if (travel.pokemon.isTeamLeader() && travel.running)
			{// Checking if switching with ally
				if (travel.destination.getPokemon() != null && !travel.destination.getPokemon().isAlliedWith(travel.pokemon)) this.runners.add(travel.pokemon);
			}
		}

		if (!(event instanceof BellyChangedEvent || event instanceof TurnSkippedEvent || event instanceof PokemonRotateEvent
				|| event instanceof PokemonTravelEvent || event instanceof PokemonSpawnedEvent))
			this.runners.clear();

		if (event.isValid()) this.doProcess(event);
		if (this.state() == State.PROCESSING) this.processPending();
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
					if (AIUtils.shouldStopRunning(actor))
					{
						this.runners.clear();
						this.setState(State.AWATING_INPUT);
						return;
					} else this.processEvent(new PokemonTravelEvent(this.dungeon.currentFloor(), actor, true, actor.facing()));
				} else
				{
					this.setState(State.AWATING_INPUT);
					return;
				}
			} else this.processEvent(this.dungeon.currentFloor().aiManager.takeAction(actor));
		}
	}

	protected void setState(State state)
	{
		this.state = state;
	}

	public State state()
	{
		return this.state;
	}

}

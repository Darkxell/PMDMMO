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
		DELAYED,
		PROCESSING
	}

	public final DungeonInstance dungeon;
	/** Pending events to process. */
	protected final Stack<DungeonEvent> pending = new Stack<DungeonEvent>();
	/** Lists the Players currently running. */
	private ArrayList<DungeonPokemon> runners = new ArrayList<>();
	/** While processing an event, setting this to false will stop processing the pending events. */
	private State state = State.PROCESSING;

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
		this.dungeon.eventOccured(event);
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

	/** Called just before processing an event.
	 * 
	 * @return false if the event should not be processed. */
	protected boolean preProcess(DungeonEvent event)
	{
		if (event instanceof PokemonTravelEvent)
		{
			PokemonTravelEvent travel = (PokemonTravelEvent) event;
			if (travel.pokemon.isTeamLeader() && travel.running)
			{// Checking if switching with ally
				if (travel.destination.getPokemon() == null || !travel.destination.getPokemon().isAlliedWith(travel.pokemon)) this.runners.add(travel.pokemon);
			}
		}

		if (this.stopsTravel(event)) this.runners.clear();

		return event.isValid();
	}

	/** Processes the input event and adds the resulting events to the pending stack. */
	public void processEvent(DungeonEvent event)
	{
		this.setState(State.PROCESSING);
		if (this.preProcess(event))
		{
			this.dungeon.consumeTurn(event.actor);
			if (this.state() == State.PROCESSING) this.doProcess(event);
		}
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

	public boolean stopsTravel(DungeonEvent event)
	{
		return !(event instanceof BellyChangedEvent || event instanceof TurnSkippedEvent || event instanceof PokemonRotateEvent
				|| event instanceof PokemonTravelEvent || event instanceof PokemonSpawnedEvent);
	}

}

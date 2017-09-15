package com.darkxell.client.mechanics;

import java.util.Stack;

import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.MoveRenderer;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.common.event.DamageDealtEvent;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.FaintedPokemonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;

/** Translates game logic events into displayable content to the client.<br />
 * Takes in Events to display messages, manage resources or change game states. */
public class DungeonEventProcessor
{

	/** Pending events to process. */
	private static final Stack<DungeonEvent> pending = new Stack<DungeonEvent>();
	/** While processing an event, setting this false will stop processing the pending events. */
	private static boolean processPending = true;

	/** Adds the input event(s) to the pending stack, without processing them. */
	public static void addToPending(DungeonEvent... events)
	{
		for (DungeonEvent e : events)
			pending.add(e);
	}

	private static void processDamageEvent(DamageDealtEvent event)
	{
		DungeonPokemonRenderer.instance.getSprite(event.target).setState(PokemonSprite.STATE_HURT);
		DungeonPersistance.dungeonState.setSubstate(new DelayState(DungeonPersistance.dungeonState, PokemonSprite.FRAMELENGTH));
		processPending = false;
	}

	/** Processes the input event and adds the resulting events to the pending stack. */
	public static void processEvent(DungeonEvent event)
	{
		processPending = true;
		addToPending(event.processServer());
		DungeonPersistance.dungeonState.logger.showMessages(event.getMessages());

		if (event instanceof MoveSelectionEvent) processMoveEvent((MoveSelectionEvent) event);
		if (event instanceof MoveUseEvent) processMoveUseEvent((MoveUseEvent) event);
		if (event instanceof DamageDealtEvent) processDamageEvent((DamageDealtEvent) event);
		if (event instanceof FaintedPokemonEvent) processFaintedEvent((FaintedPokemonEvent) event);

		if (processPending) processPending();
	}

	private static void processFaintedEvent(FaintedPokemonEvent event)
	{
		DungeonPokemonRenderer.instance.unregister(event.pokemon);
	}

	private static void processMoveEvent(MoveSelectionEvent event)
	{
		AnimationState s = new AnimationState(DungeonPersistance.dungeonState);
		s.animation = MoveRenderer.createAnimation(s, event.user, event.move.move());
		DungeonPersistance.dungeonState.setSubstate(s);
		processPending = false;
	}

	private static void processMoveUseEvent(MoveUseEvent event)
	{
		AnimationState s = new AnimationState(DungeonPersistance.dungeonState);
		s.animation = MoveRenderer.createTargetAnimation(s, event.user, event.move.move());
		if (s.animation != null)
		{
			DungeonPersistance.dungeonState.setSubstate(s);
			processPending = false;
		}
	}

	/** Processes the next pending event. */
	public static void processPending()
	{
		if (!pending.empty()) processEvent(pending.pop());
	}

}

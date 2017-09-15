package com.darkxell.client.mechanics;

import java.util.Stack;

import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.client.state.dungeon.MoveAnimationState;
import com.darkxell.common.event.DamageDealtEvent;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.util.Message;

/** Translates game logic event into displayable content to the client.<br />
 * Takes in Events to display messages or change game states. */
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
		DungeonPersistance.dungeonState.logger.showMessage(new Message("move.damage_dealt").addReplacement("<pokemon>", event.target.pokemon.getNickname())
				.addReplacement("<amount>", Integer.toString(event.damage)));
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
		if (event instanceof DamageDealtEvent) processDamageEvent((DamageDealtEvent) event);

		if (processPending) processPending();
	}

	private static void processMoveEvent(MoveSelectionEvent event)
	{
		DungeonPersistance.dungeonState.setSubstate(new MoveAnimationState(DungeonPersistance.dungeonState, event.user, event.move));
		processPending = false;
	}

	/** Processes the next pending event. */
	public static void processPending()
	{
		if (!pending.empty()) processEvent(pending.pop());
	}

}

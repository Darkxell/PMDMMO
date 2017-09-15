package com.darkxell.client.mechanics;

import java.util.Stack;

import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.MoveTarget;
import com.darkxell.common.event.MoveUseEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.Message;

/** Translates game logic event into displayable content to the client.<br />
 * Takes in Events to display messages or change game states. */
public class DungeonEventProcessor
{

	/** Pending events to process. */
	private static final Stack<Event> pending = new Stack<Event>();
	/** While processing an event, setting this false will stop processing the pending events. */
	private static boolean processPending = true;

	/** Processes the input event and adds the resulting events to the pending stack. */
	public static void processEvent(Event event)
	{
		for (Event result : event.getResultingEvents())
			pending.push(result);

		processPending = true;
		if (event instanceof MoveUseEvent) processMoveEvent((MoveUseEvent) event);

		if (processPending) processPending();
	}

	private static void processMoveEvent(MoveUseEvent event)
	{
		if (event.targets().length == 0)
		{
			if (event.move != MoveRegistry.ATTACK) DungeonPersistance.dungeonState.logger.showMessage(new Message("move.no_target"));
		} else
		{
			for (MoveTarget target : event.targets())
			{
				DungeonPersistance.dungeonState.logger.showMessage(target.resultMessage());
				DungeonPokemonRenderer.instance.getSprite(target.pokemon).setState(PokemonSprite.STATE_HURT);
			}

			DungeonPersistance.dungeonState.setSubstate(new DelayState(DungeonPersistance.dungeonState, PokemonSprite.FRAMELENGTH));
			processPending = false;
		}
	}

	/** Processes the next pending event. */
	public static void processPending()
	{
		if (!pending.empty()) processEvent(pending.pop());
	}

}

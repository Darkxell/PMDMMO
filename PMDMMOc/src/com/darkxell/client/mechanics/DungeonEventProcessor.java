package com.darkxell.client.mechanics;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.ItemRenderer;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.common.event.*;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.item.ItemUseSelectionEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.stats.StatChangedEvent;

/** Translates game logic events into displayable content to the client.<br />
 * Takes in Events to display messages, manage resources or change game states. */
public final class DungeonEventProcessor
{
	/** Pending events to process. */
	private static final Stack<DungeonEvent> pending = new Stack<DungeonEvent>();
	/** While processing an event, setting this false will stop processing the pending events. */
	static boolean processPending = true;

	/** Adds the input event(s) to the pending stack, without processing them. */
	public static void addToPending(ArrayList<DungeonEvent> arrayList)
	{
		for (int i = arrayList.size() - 1; i >= 0; --i)
			pending.add(arrayList.get(i));
	}

	private static void processDungeonExitEvent(DungeonExitEvent event)
	{
		if (event.pokemon == DungeonPersistance.player.getDungeonPokemon()) Launcher.stateManager.setState(new FreezoneExploreState());
	}

	/** Processes the input event and adds the resulting events to the pending stack. */
	public static void processEvent(DungeonEvent event)
	{
		processPending = true;

		if (event.isValid())
		{
			addToPending(event.processServer());

			if (event instanceof MoveSelectionEvent) MoveEventProcessor.processMoveEvent((MoveSelectionEvent) event);
			if (event instanceof MoveUseEvent) MoveEventProcessor.processMoveUseEvent((MoveUseEvent) event);
			if (event instanceof DamageDealtEvent) MoveEventProcessor.processDamageEvent((DamageDealtEvent) event);
			if (event instanceof StatChangedEvent) MoveEventProcessor.processStatEvent((StatChangedEvent) event);
			if (event instanceof FaintedPokemonEvent) MoveEventProcessor.processFaintedEvent((FaintedPokemonEvent) event);

			if (event instanceof ItemUseSelectionEvent) processItemEvent((ItemUseSelectionEvent) event);

			if (event instanceof DungeonExitEvent) processDungeonExitEvent((DungeonExitEvent) event);

			DungeonPersistance.dungeonState.logger.showMessages(event.getMessages());
		}

		if (processPending) processPending();
	}

	private static void processItemEvent(ItemUseSelectionEvent event)
	{
		AnimationState a = new AnimationState(DungeonPersistance.dungeonState);
		a.animation = ItemRenderer.createItemAnimation(event, a);
		DungeonPersistance.dungeonState.setSubstate(a);
		processPending = false;
	}

	/** Processes the next pending event. */
	public static void processPending()
	{
		if (!pending.empty()) processEvent(pending.pop());
	}

	private DungeonEventProcessor()
	{}

}

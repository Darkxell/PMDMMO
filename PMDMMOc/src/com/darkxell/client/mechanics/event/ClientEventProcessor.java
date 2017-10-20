package com.darkxell.client.mechanics.event;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.renderers.ItemAnimationRenderer;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogEndListener;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.dungeon.PokemonTravelState;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.client.state.menu.dungeon.StairMenuState;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;
import com.darkxell.common.event.item.ItemUseSelectionEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;

/** Translates game logic events into displayable content to the client.<br />
 * Takes in Events to display messages, manage resources or change game states. */
public final class ClientEventProcessor extends CommonEventProcessor
{
	/** Pending events to process. */
	public static final AnimationEndListener processEventsOnAnimationEnd = new AnimationEndListener()
	{
		@Override
		public void onAnimationEnd(AbstractAnimation animation)
		{
			if (!Persistance.eventProcessor.hasPendingEvents()) Persistance.stateManager.setState(Persistance.dungeonState);
			else Persistance.eventProcessor.processPending();
		}
	};

	public static final DialogEndListener processEventsOnDialogEnd = new DialogEndListener()
	{
		@Override
		public void onDialogEnd(DialogState dialog)
		{
			if (!Persistance.eventProcessor.hasPendingEvents()) Persistance.stateManager.setState(Persistance.dungeonState);
			else Persistance.eventProcessor.processPending();
		}
	};

	public boolean landedOnStairs = false;

	public ClientEventProcessor(DungeonInstance dungeon)
	{
		super(dungeon);
	}

	@Override
	public void doProcess(DungeonEvent event)
	{
		super.doProcess(event);
		Persistance.dungeonState.logger.showMessages(event.getMessages());

		if (event instanceof MoveSelectionEvent) MoveEvents.processMoveEvent((MoveSelectionEvent) event);
		if (event instanceof MoveUseEvent) MoveEvents.processMoveUseEvent((MoveUseEvent) event);
		if (event instanceof DamageDealtEvent) MoveEvents.processDamageEvent((DamageDealtEvent) event);

		if (event instanceof PokemonTravelEvent) this.processTravelEvent((PokemonTravelEvent) event);
		if (event instanceof FaintedPokemonEvent) MoveEvents.processFaintedEvent((FaintedPokemonEvent) event);

		if (event instanceof StatChangedEvent) MoveEvents.processStatEvent((StatChangedEvent) event);
		if (event instanceof TriggeredAbilityEvent) MoveEvents.processAbilityEvent((TriggeredAbilityEvent) event);
		if (event instanceof ExperienceGainedEvent) MoveEvents.processExperienceEvent((ExperienceGainedEvent) event);

		if (event instanceof ItemUseSelectionEvent) this.processItemEvent((ItemUseSelectionEvent) event);

		if (event instanceof StairLandingEvent) this.processStairEvent((StairLandingEvent) event);
		if (event instanceof NextFloorEvent) this.processFloorEvent((NextFloorEvent) event);
		if (event instanceof DungeonExitEvent) this.processDungeonExitEvent((DungeonExitEvent) event);
	}

	@Override
	protected void onTurnEnd()
	{
		if (this.landedOnStairs)
		{
			addToPending(new StairLandingEvent());
			this.landedOnStairs = false;
		}
		super.onTurnEnd();
	}

	private void processDungeonExitEvent(DungeonExitEvent event)
	{
		if (event.pokemon == Persistance.player.getDungeonPokemon())
		{
			Persistance.stateManager.setState(new FreezoneExploreState());
			Persistance.displaymap = LocalMap.instance;
		}
	}

	private void processFloorEvent(NextFloorEvent event)
	{
		this.processPending = false;
		Persistance.stateManager.setState(new NextFloorState(event.floor.id + 1));
	}

	private void processItemEvent(ItemUseSelectionEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		a.animation = ItemAnimationRenderer.createItemAnimation(event, a);
		Persistance.dungeonState.setSubstate(a);
		this.processPending = false;
	}

	private void processStairEvent(StairLandingEvent event)
	{
		this.processPending = false;
		Persistance.stateManager.setState(new StairMenuState());
	}

	private void processTravelEvent(PokemonTravelEvent event)
	{
		this.processPending = false;
		Persistance.dungeonState.setSubstate(new PokemonTravelState(Persistance.dungeonState, event.isRunning(), event.travels()));
	}

}

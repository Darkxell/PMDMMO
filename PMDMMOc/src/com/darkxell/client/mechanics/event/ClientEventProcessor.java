package com.darkxell.client.mechanics.event;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.misc.RainAnimation;
import com.darkxell.client.mechanics.animation.misc.SnowAnimation;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogEndListener;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.dungeon.PokemonTravelState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.client.state.menu.dungeon.StairMenuState;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.TurnSkippedEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.event.item.ItemUseSelectionEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.*;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.ItemFood;
import com.darkxell.common.item.ItemGummi;
import com.darkxell.common.weather.Weather;

/** Translates game logic events into displayable content to the client.<br />
 * Takes in Events to display messages, manage resources or change game states. */
public final class ClientEventProcessor extends CommonEventProcessor
{
	/** Pending events to process. */
	public static final AnimationEndListener processEventsOnAnimationEnd = new AnimationEndListener() {
		@Override
		public void onAnimationEnd(AbstractAnimation animation)
		{
			if (!Persistance.eventProcessor.hasPendingEvents())
			{
				if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(Persistance.dungeonState);
			} else Persistance.eventProcessor.processPending();
		}
	};

	public static final DialogEndListener processEventsOnDialogEnd = new DialogEndListener() {
		@Override
		public void onDialogEnd(DialogState dialog)
		{
			if (!Persistance.eventProcessor.hasPendingEvents())
			{
				if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(Persistance.dungeonState);
			} else Persistance.eventProcessor.processPending();
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
		if (event instanceof StatusConditionCreatedEvent) MoveEvents.processStatusEvent((StatusConditionCreatedEvent) event);
		if (event instanceof StatusConditionEndedEvent) MoveEvents.processStatusEvent((StatusConditionEndedEvent) event);

		if (event instanceof PokemonSpawnedEvent) this.processSpawnEvent((PokemonSpawnedEvent) event);
		if (event instanceof PokemonTravelEvent) this.processTravelEvent((PokemonTravelEvent) event);
		if (event instanceof FaintedPokemonEvent) MoveEvents.processFaintedEvent((FaintedPokemonEvent) event);

		if (event instanceof StatChangedEvent) MoveEvents.processStatEvent((StatChangedEvent) event);
		if (event instanceof TriggeredAbilityEvent) MoveEvents.processAbilityEvent((TriggeredAbilityEvent) event);
		if (event instanceof ExperienceGainedEvent) MoveEvents.processExperienceEvent((ExperienceGainedEvent) event);

		if (event instanceof ItemUseSelectionEvent) this.processItemEvent((ItemUseSelectionEvent) event);
		if (event instanceof ItemMovedEvent) this.processItemMovedEvent((ItemMovedEvent) event);
		if (event instanceof ItemSwappedEvent) this.processItemSwappedEvent((ItemSwappedEvent) event);

		if (event instanceof WeatherChangedEvent) this.processWeatherEvent((WeatherChangedEvent) event);
		if (event instanceof StairLandingEvent) this.processStairEvent((StairLandingEvent) event);
		if (event instanceof NextFloorEvent) this.processFloorEvent((NextFloorEvent) event);
		if (event instanceof DungeonExitEvent) this.processDungeonExitEvent((DungeonExitEvent) event);
	}

	@Override
	protected void onTurnEnd()
	{
		if (this.landedOnStairs)
		{
			this.addToPending(new StairLandingEvent());
			this.landedOnStairs = false;
		}
		super.onTurnEnd();
	}

	private void processDungeonExitEvent(DungeonExitEvent event)
	{
		if (event.pokemon == Persistance.player.getDungeonLeader())
		{
			if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(new FreezoneExploreState());
			Persistance.displaymap = LocalMap.instance;
		}
	}

	private void processFloorEvent(NextFloorEvent event)
	{
		this.processPending = false;
		if (Persistance.stateManager instanceof PrincipalMainState)
			((PrincipalMainState) Persistance.stateManager).setState(new NextFloorState(Persistance.dungeonState, event.floor.id + 1));
	}

	private void processItemEvent(ItemUseSelectionEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		if (event.item instanceof ItemFood || event.item instanceof ItemGummi) a.animation = SpritesetAnimation.getCustomAnimation(event.user, 0, a);
		else a.animation = SpritesetAnimation.getItemAnimation(event.user, event.item, a);
		if (a.animation != null)
		{
			Persistance.dungeonState.setSubstate(a);
			this.processPending = false;
		}
	}

	private void processItemMovedEvent(ItemMovedEvent event)
	{
		if (event.source instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.source);
	}

	private void processItemSwappedEvent(ItemSwappedEvent event)
	{
		if (event.source instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.source);
		else if (event.destination instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.destination);
	}

	private void processSpawnEvent(PokemonSpawnedEvent event)
	{
		PokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.register(event.spawned);
		if (event.spawned.pokemon.player != null)
		{
			if (event.spawned.pokemon.player != Persistance.player)
			{
				if (event.spawned.isTeamLeader()) renderer.sprite.setShadowColor(PokemonSprite.PLAYER_SHADOW);
				else renderer.sprite.setShadowColor(PokemonSprite.ENEMY_SHADOW);
			} else renderer.sprite.setShadowColor(PokemonSprite.ALLY_SHADOW);
		}
	}

	private void processStairEvent(StairLandingEvent event)
	{
		this.processPending = false;
		if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(new StairMenuState());
	}

	private void processTravelEvent(PokemonTravelEvent event)
	{
		this.processPending = false;
		Persistance.dungeonState.setSubstate(new PokemonTravelState(Persistance.dungeonState, event.isRunning(), event.travels()));
		for (PokemonTravel travel : event.travels())
			if (travel.pokemon == Persistance.dungeonState.getCameraPokemon()) Persistance.dungeonState.floorVisibility.onCameraMoved();
	}

	private void processWeatherEvent(WeatherChangedEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		if (event.next.weather == Weather.RAIN) a.animation = new RainAnimation(100, a);
		else if (event.next.weather == Weather.SNOW) a.animation = new SnowAnimation(a);
		else if (event.next.weather == Weather.HAIL) a.animation = new RainAnimation(103, a);
		else if (event.next.weather == Weather.SUNNY) a.animation = SpritesetAnimation.getCustomAnimation(null, 101, a);
		if (a.animation != null)
		{
			Persistance.dungeonState.setSubstate(a);
			this.processPending = false;
		}
	}

	public boolean shouldStopMoving()
	{
		for (DungeonEvent event : this.pending)
			if (!(event instanceof BellyChangedEvent || event instanceof TurnSkippedEvent || event instanceof PokemonRotateEvent)) return true;
		return false;
	}

}

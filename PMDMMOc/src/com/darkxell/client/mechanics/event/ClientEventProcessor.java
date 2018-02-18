package com.darkxell.client.mechanics.event;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.StatChangeAnimation;
import com.darkxell.client.mechanics.animation.misc.RainAnimation;
import com.darkxell.client.mechanics.animation.misc.SnowAnimation;
import com.darkxell.client.renderers.AbilityAnimationRenderer;
import com.darkxell.client.renderers.MoveRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogEndListener;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.dungeon.PokemonTravelState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.client.state.menu.dungeon.MoveLearnMenuState;
import com.darkxell.client.state.menu.dungeon.StairMenuState;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.event.item.ItemUseSelectionEvent;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.move.MoveLearnedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.BellyChangedEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.PokemonSpawnedEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.LevelupEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.ItemFood;
import com.darkxell.common.item.ItemGummi;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonStats;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
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
			if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(Persistance.dungeonState);
			if (Persistance.eventProcessor.hasPendingEvents()) Persistance.eventProcessor.processPending();
		}
	};

	public static final DialogEndListener processEventsOnDialogEnd = new DialogEndListener() {
		@Override
		public void onDialogEnd(DialogState dialog)
		{
			if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(Persistance.dungeonState);
			if (Persistance.eventProcessor.hasPendingEvents()) Persistance.eventProcessor.processPending();
		}
	};

	public boolean landedOnStairs = false;
	private PokemonStats levelupStats = null;

	public ClientEventProcessor(DungeonInstance dungeon)
	{
		super(dungeon);
	}

	@Override
	public void doProcess(DungeonEvent event)
	{
		super.doProcess(event);
		Persistance.dungeonState.logger.showMessages(event.getMessages());
		Logger.event(event.loggerMessage());

		if (event instanceof MoveSelectionEvent) this.processMoveEvent((MoveSelectionEvent) event);
		if (event instanceof MoveUseEvent) this.processMoveUseEvent((MoveUseEvent) event);
		if (event instanceof DamageDealtEvent) this.processDamageEvent((DamageDealtEvent) event);
		if (event instanceof StatusConditionCreatedEvent) this.processStatusEvent((StatusConditionCreatedEvent) event);
		if (event instanceof StatusConditionEndedEvent) this.processStatusEvent((StatusConditionEndedEvent) event);

		if (event instanceof PokemonSpawnedEvent) this.processSpawnEvent((PokemonSpawnedEvent) event);
		if (event instanceof PokemonTravelEvent) this.processTravelEvent((PokemonTravelEvent) event);
		if (event instanceof FaintedPokemonEvent) this.processFaintedEvent((FaintedPokemonEvent) event);

		if (event instanceof StatChangedEvent) this.processStatEvent((StatChangedEvent) event);
		if (event instanceof TriggeredAbilityEvent) this.processAbilityEvent((TriggeredAbilityEvent) event);
		if (event instanceof LevelupEvent) this.processLevelupEvent((LevelupEvent) event);
		if (event instanceof MoveDiscoveredEvent) this.processMoveDiscoveredEvent((MoveDiscoveredEvent) event);
		if (event instanceof MoveLearnedEvent) this.processMoveLearnedEvent((MoveLearnedEvent) event);

		if (event instanceof ItemUseSelectionEvent) this.processItemEvent((ItemUseSelectionEvent) event);
		if (event instanceof ItemMovedEvent) this.processItemMovedEvent((ItemMovedEvent) event);
		if (event instanceof ItemSwappedEvent) this.processItemSwappedEvent((ItemSwappedEvent) event);

		if (event instanceof WeatherChangedEvent) this.processWeatherEvent((WeatherChangedEvent) event);
		if (event instanceof StairLandingEvent) this.processStairEvent((StairLandingEvent) event);
		if (event instanceof NextFloorEvent) this.processFloorEvent((NextFloorEvent) event);
		if (event instanceof DungeonExitEvent) this.processDungeonExitEvent((DungeonExitEvent) event);
	}

	@Override
	public void onTurnEnd()
	{
		if (this.landedOnStairs)
		{
			this.addToPending(new StairLandingEvent());
			this.landedOnStairs = false;
		}
		Logger.event("Turn ended ---------------");
		super.onTurnEnd();
	}

	private void processAbilityEvent(TriggeredAbilityEvent event)
	{
		AbstractAnimation animation = AbilityAnimationRenderer.createAnimation(event);
		if (animation != null && animation.needsPause)
		{
			AnimationState s = new AnimationState(Persistance.dungeonState);
			s.animation = animation;
			Persistance.dungeonState.setSubstate(s);
			this.processPending = false;
		} else animation.start();
	}

	private void processDamageEvent(DamageDealtEvent event)
	{
		if (!(event.source instanceof BellyChangedEvent))
		{
			Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite.setState(PokemonSprite.STATE_HURT);
			Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite.setHealthChange(-event.damage);
			Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, PokemonSprite.FRAMELENGTH));
			this.processPending = false;
		}
	}

	private void processDungeonExitEvent(DungeonExitEvent event)
	{
		if (event.pokemon == Persistance.player.getDungeonLeader())
		{
			if (Persistance.stateManager instanceof PrincipalMainState) ((PrincipalMainState) Persistance.stateManager).setState(new FreezoneExploreState());
			Persistance.displaymap = LocalMap.instance;
		}
	}

	private void processFaintedEvent(FaintedPokemonEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.unregister(event.pokemon);
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

	private void processLevelupEvent(LevelupEvent event)
	{
		Pokemon pokemon = event.pokemon;
		if (Persistance.player.isAlly(pokemon) && Persistance.stateManager instanceof PrincipalMainState)
		{
			this.processPending = false;
			boolean firstLevel = this.levelupStats == null;

			if (this.levelupStats == null) this.levelupStats = pokemon.species.baseStatsIncrease(pokemon.getLevel() - 1);
			else this.levelupStats.add(pokemon.species.baseStatsIncrease(pokemon.getLevel() - 1));

			ArrayList<DialogScreen> screens = new ArrayList<DialogScreen>();
			screens.add(new DialogScreen(new Message("xp.levelup").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<level>",
					Integer.toString(pokemon.getLevel()))));

			{
				boolean hasMoreLevels = false;
				for (DungeonEvent e : this.pending)
					if (e instanceof LevelupEvent && ((LevelupEvent) e).pokemon == pokemon)
					{
						hasMoreLevels = true;
						break;
					}
				if (!hasMoreLevels)
				{
					screens.add(new DialogScreen(new Message("xp.stats").addReplacement("<atk>", TextRenderer.alignNumber(this.levelupStats.getAttack(), 2))
							.addReplacement("<def>", TextRenderer.alignNumber(this.levelupStats.getDefense(), 2))
							.addReplacement("<hea>", TextRenderer.alignNumber(this.levelupStats.getHealth(), 2))
							.addReplacement("<spa>", TextRenderer.alignNumber(this.levelupStats.getSpecialAttack(), 2))
							.addReplacement("<spd>", TextRenderer.alignNumber(this.levelupStats.getSpecialDefense(), 2))));
					this.levelupStats = null;
				}
			}

			DialogState state = new DialogState(Persistance.dungeonState, processEventsOnDialogEnd, false, screens);

			if (firstLevel)
			{
				SoundManager.playSoundOverMusic("game-levelup");
				Persistance.dungeonState.setSubstate(
						new DelayState(Persistance.dungeonState, 60, (DelayState s) -> ((PrincipalMainState) Persistance.stateManager).setState(state)));
			} else((PrincipalMainState) Persistance.stateManager).setState(state);
		}
	}

	private void processMoveDiscoveredEvent(MoveDiscoveredEvent event)
	{
		if (event.pokemon.moveCount() == 4 && Persistance.stateManager instanceof PrincipalMainState)
		{
			this.processPending = false;

			DialogEndListener listener = new DialogEndListener() {

				@Override
				public void onDialogEnd(DialogState dialog)
				{
					((PrincipalMainState) Persistance.stateManager).setState(new MoveLearnMenuState(Persistance.dungeonState, event.pokemon, event.move));
				}
			};

			((PrincipalMainState) Persistance.stateManager).setState(new DialogState(Persistance.dungeonState, listener, false, new DialogScreen(
					new Message("moves.learned.full").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<move>", event.move.name()))));
		}
	}

	private void processMoveEvent(MoveSelectionEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = MoveRenderer.createAnimation(s, event.usedMove.user, event.usedMove.move.move());
		Persistance.dungeonState.setSubstate(s);
		this.processPending = false;
	}

	private void processMoveLearnedEvent(MoveLearnedEvent event)
	{
		if (Persistance.player.isAlly(event.pokemon) && Persistance.stateManager instanceof PrincipalMainState)
		{
			this.processPending = false;
			((PrincipalMainState) Persistance.stateManager).setState(new DialogState(Persistance.dungeonState, ClientEventProcessor.processEventsOnDialogEnd,
					false, new DialogScreen(new Message("moves.learned").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<move>",
							event.move.name()))));
		}
	}

	private void processMoveUseEvent(MoveUseEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = MoveRenderer.createTargetAnimation(s, event.usedMove.user, event.usedMove.move.move());
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.processPending = false;
		}
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

	private void processStatEvent(StatChangedEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = new StatChangeAnimation(s, event.target, event.stat, event.stage);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.processPending = false;
		}
	}

	private void processStatusEvent(StatusConditionCreatedEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		AnimationEndListener end = new AnimationEndListener() {
			@Override
			public void onAnimationEnd(AbstractAnimation animation)
			{
				if (animation != null) s.onAnimationEnd(animation);
				AbstractAnimation a = SpritesetAnimation.getStatusAnimation(event.condition.pokemon, event.condition.condition, null);
				if (a != null)
				{
					a.source = event.condition;
					a.start();
				}
			}
		};
		s.animation = SpritesetAnimation.getCustomAnimation(event.condition.pokemon, 200 + event.condition.condition.id, end);
		if (s.animation == null) end.onAnimationEnd(null);
		else
		{
			Persistance.dungeonState.setSubstate(s);
			this.processPending = false;
		}
	}

	private void processStatusEvent(StatusConditionEndedEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.getRenderer(event.condition.pokemon).removeAnimation(event.condition);
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

}

package com.darkxell.client.mechanics.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.misc.RainAnimation;
import com.darkxell.client.mechanics.animation.misc.SnowAnimation;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.dialog.AbstractDialogState;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogEndListener;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.dungeon.OrbAnimationState;
import com.darkxell.client.state.dungeon.PokemonTravelState;
import com.darkxell.client.state.menu.dungeon.MoveLearnMenuState;
import com.darkxell.client.state.menu.dungeon.StairMenuState;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.PokemonSpawnedEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;
import com.darkxell.common.event.dungeon.PlayerLosesEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.event.item.MoneyCollectedEvent;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.move.MoveLearnedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.event.stats.LevelupEvent;
import com.darkxell.common.event.stats.SpeedChangedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.ItemFood;
import com.darkxell.common.item.ItemGummi;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.pokemon.BaseStats;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

/** Translates game logic events into displayable content to the client.<br />
 * Takes in Events to display messages, manage resources or change game states. */
public final class ClientEventProcessor extends CommonEventProcessor
{
	public static final AnimationEndListener animateDelayedOnAnimationEnd = new AnimationEndListener() {

		@Override
		public void onAnimationEnd(AbstractAnimation animation)
		{
			Persistance.eventProcessor.animateDelayed();
		}
	};
	/** Pending events to process. */
	public static final AnimationEndListener processEventsOnAnimationEnd = new AnimationEndListener() {
		@Override
		public void onAnimationEnd(AbstractAnimation animation)
		{
			Persistance.eventProcessor.processPending();
		}
	};

	public static final DialogEndListener processEventsOnDialogEnd = new DialogEndListener() {
		@Override
		public void onDialogEnd(AbstractDialogState dialog)
		{
			Persistance.stateManager.setState(Persistance.dungeonState);
			Persistance.eventProcessor.processPending();
		}
	};

	private AnimationEndListener currentAnimEnd = processEventsOnAnimationEnd;
	/** Stores events that animate at the same time as the travel events. */
	private Stack<DungeonEvent> delayedWithTravels = new Stack<>();
	private boolean landedOnStairs = false;
	private BaseStats levelupStats = null;
	/** Stores consecutive travel events to animate them at the same time. */
	private ArrayList<PokemonTravelEvent> travels = new ArrayList<>();

	public ClientEventProcessor(DungeonInstance dungeon)
	{
		super(dungeon);
	}

	public void animateDelayed()
	{
		this.setState(State.DELAYED);
		if (!this.travels.isEmpty())
		{
			this.currentAnimEnd = animateDelayedOnAnimationEnd;
			PokemonTravelsEvent e = new PokemonTravelsEvent(this.dungeon.currentFloor(), this.travels);
			this.travels.clear();
			this.doClientProcess(e);
		} else if (!this.delayedWithTravels.isEmpty()) this.doClientProcess(this.delayedWithTravels.pop());
		else
		{
			this.setState(State.PROCESSING);
			this.currentAnimEnd = processEventsOnAnimationEnd;
			this.processPending();
		}
	}

	private void doClientProcess(DungeonEvent event)
	{
		if (this.delayedWithTravels.contains(event)) return;

		if (event.displayMessages) Persistance.dungeonState.logger.showMessages(event.getMessages());
		Logger.event(event.loggerMessage());

		if (event instanceof MoveSelectionEvent) this.processMoveEvent((MoveSelectionEvent) event);
		if (event instanceof MoveUseEvent) this.processMoveUseEvent((MoveUseEvent) event);
		if (event instanceof DamageDealtEvent) this.processDamageEvent((DamageDealtEvent) event);
		if (event instanceof HealthRestoredEvent) this.processHealEvent((HealthRestoredEvent) event);
		if (event instanceof StatusConditionCreatedEvent) this.processStatusEvent((StatusConditionCreatedEvent) event);
		if (event instanceof StatusConditionEndedEvent) this.processStatusEvent((StatusConditionEndedEvent) event);

		if (event instanceof PokemonSpawnedEvent) this.processSpawnEvent((PokemonSpawnedEvent) event);
		if (event instanceof PokemonTravelsEvent) this.processTravelEvent((PokemonTravelsEvent) event);
		if (event instanceof FaintedPokemonEvent) this.processFaintedEvent((FaintedPokemonEvent) event);

		if (event instanceof StatChangedEvent) this.processStatEvent((StatChangedEvent) event);
		if (event instanceof SpeedChangedEvent) this.processSpeedEvent((SpeedChangedEvent) event);
		if (event instanceof TriggeredAbilityEvent) this.processAbilityEvent((TriggeredAbilityEvent) event);
		if (event instanceof LevelupEvent) this.processLevelupEvent((LevelupEvent) event);
		if (event instanceof MoveDiscoveredEvent) this.processMoveDiscoveredEvent((MoveDiscoveredEvent) event);
		if (event instanceof MoveLearnedEvent) this.processMoveLearnedEvent((MoveLearnedEvent) event);

		if (event instanceof ItemSelectionEvent) this.processItemEvent((ItemSelectionEvent) event);
		if (event instanceof ItemMovedEvent) this.processItemMovedEvent((ItemMovedEvent) event);
		if (event instanceof ItemSwappedEvent) this.processItemSwappedEvent((ItemSwappedEvent) event);
		if (event instanceof MoneyCollectedEvent && Persistance.player.isAlly(((MoneyCollectedEvent) event).pokemon)) SoundManager.playSound("dungeon-money");

		if (event instanceof WeatherChangedEvent) this.processWeatherEvent((WeatherChangedEvent) event);
		if (event instanceof StairLandingEvent) this.processStairEvent((StairLandingEvent) event);
		if (event instanceof NextFloorEvent) this.processFloorEvent((NextFloorEvent) event);
		if (event instanceof DungeonExitEvent) this.processDungeonExitEvent((DungeonExitEvent) event);
		if (event instanceof PlayerLosesEvent) this.processPlayerLosesEvent((PlayerLosesEvent) event);

		if (this.state() == State.DELAYED) this.animateDelayed();
	}

	@Override
	public void doProcess(DungeonEvent event)
	{
		super.doProcess(event);
		this.doClientProcess(event);
	}

	@Override
	public void onTurnEnd()
	{
		if (!this.travels.isEmpty()) this.animateDelayed();
		else
		{
			if (this.landedOnStairs)
			{
				this.addToPending(new StairLandingEvent());
				this.landedOnStairs = false;
			}
			Logger.event("Turn ended ---------------");
			super.onTurnEnd();
		}
	}

	@Override
	protected boolean preProcess(DungeonEvent event)
	{
		if (event instanceof PokemonTravelEvent)
		{
			this.travels.add((PokemonTravelEvent) event);
		} else if (!this.travels.isEmpty())
		{
			if (this.stopsTravel(event))
			{
				this.addToPending(event);
				this.animateDelayed();
				return false;
			} else this.delayedWithTravels.push(event);
		}
		return super.preProcess(event);
	}

	private void processAbilityEvent(TriggeredAbilityEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getAbilityAnimation(event.pokemon, event.ability, this.currentAnimEnd);
		if (s.animation != null)
		{
			if (s.animation.needsPause)
			{
				Persistance.dungeonState.setSubstate(s);
				this.setState(State.ANIMATING);
			} else s.animation.start();
		}
	}

	private void processDamageEvent(DamageDealtEvent event)
	{
		if (!(event.source instanceof BellyChangedEvent))
		{
			SoundManager.playSound("dungeon-hurt");
			Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite().setState(PokemonSpriteState.HURT);
			Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite().setHealthChange(-event.damage);
			Animations.getCustomAnimation(event.target, Animations.HURT, null).start();
			Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, PokemonSprite.FRAMELENGTH));
			this.setState(State.ANIMATING);
		}
	}

	private void processDungeonExitEvent(DungeonExitEvent event)
	{
		if (event.pokemon == Persistance.player.getDungeonLeader())
		{
			Persistance.player.resetDungeonTeam();
			StateManager.setExploreState("Base", -1, -1);
		}
	}

	private void processFaintedEvent(FaintedPokemonEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.unregister(event.pokemon);
	}

	private void processFloorEvent(NextFloorEvent event)
	{
		this.setState(State.ANIMATING);
		this.delayedWithTravels.clear();
		Persistance.stateManager.setState(new NextFloorState(Persistance.dungeonState, event.floor.id + 1));
	}

	private void processHealEvent(HealthRestoredEvent event)
	{
		if (event.effectiveHeal() <= 0) return;
		Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite().setHealthChange(event.effectiveHeal());
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getCustomAnimation(event.target, Animations.HEAL, this.currentAnimEnd);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processItemEvent(ItemSelectionEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		if (event.item instanceof ItemFood || event.item instanceof ItemGummi) a.animation = Animations.getCustomAnimation(event.user, 0, this.currentAnimEnd);
		else a.animation = Animations.getItemAnimation(event.user, event.item, this.currentAnimEnd);
		if (a.animation != null)
		{
			Persistance.dungeonState.setSubstate(a);
			this.setState(State.ANIMATING);
		}
	}

	private void processItemMovedEvent(ItemMovedEvent event)
	{
		if (event.source instanceof Tile)
		{
			boolean ally = true;
			if (event.destination instanceof Pokemon) ally = Persistance.player.isAlly((Pokemon) event.destination);
			else if (event.destination instanceof Inventory) ally = Persistance.player.inventory() == (Inventory) event.destination;
			SoundManager.playSound(ally ? "dungeon-item" : "dungeon-enemygrab");
			Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.source);
		}
	}

	private void processItemSwappedEvent(ItemSwappedEvent event)
	{
		if (event.source instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.source);
		else if (event.destination instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.destination);
	}

	private void processLevelupEvent(LevelupEvent event)
	{
		Pokemon pokemon = event.pokemon;
		if (Persistance.player.isAlly(pokemon))
		{
			this.setState(State.ANIMATING);
			boolean firstLevel = this.levelupStats == null;

			if (this.levelupStats == null) this.levelupStats = pokemon.species().baseStatsIncrease(pokemon.level() - 1);
			else this.levelupStats.add(pokemon.species().baseStatsIncrease(pokemon.level() - 1));

			ArrayList<DialogScreen> screens = new ArrayList<DialogScreen>();
			screens.add(new DialogScreen(
					new Message("xp.levelup").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<level>", Integer.toString(pokemon.level()))));

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
				Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, 60, (DelayState s) -> Persistance.stateManager.setState(state)));
			} else Persistance.stateManager.setState(state);
		}
	}

	private void processMoveDiscoveredEvent(MoveDiscoveredEvent event)
	{
		if (event.pokemon.moveCount() == 4)
		{
			this.setState(State.ANIMATING);

			DialogEndListener listener = new DialogEndListener() {

				@Override
				public void onDialogEnd(AbstractDialogState dialog)
				{
					Persistance.stateManager.setState(new MoveLearnMenuState(Persistance.dungeonState, event.pokemon, event.move));
				}
			};

			Persistance.stateManager.setState(new DialogState(Persistance.dungeonState, listener, false, new DialogScreen(
					new Message("moves.learned.full").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<move>", event.move.name()))));
		}
	}

	private void processMoveEvent(MoveSelectionEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getMoveAnimation(event.usedMove.user, event.usedMove.move.move(), this.currentAnimEnd);
		if (s.animation != null)
		{
			if (Animations.playsOrbAnimation(event.usedMove.user, event.usedMove.move.move()))
				s = new OrbAnimationState(Persistance.dungeonState, event.usedMove.user, s);

			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processMoveLearnedEvent(MoveLearnedEvent event)
	{
		if (Persistance.player.isAlly(event.pokemon))
		{
			this.setState(State.ANIMATING);
			SoundManager.playSound("game-movelearned");
			Persistance.stateManager.setState(new DialogState(Persistance.dungeonState, ClientEventProcessor.processEventsOnDialogEnd, false, new DialogScreen(
					new Message("moves.learned").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<move>", event.move.name()))));
		}
	}

	private void processMoveUseEvent(MoveUseEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getMoveTargetAnimation(event.target, event.usedMove.move.move(), this.currentAnimEnd);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processPlayerLosesEvent(PlayerLosesEvent event)
	{
		if (event.player == Persistance.player)
		{
			StateManager.setExploreState("Base", -1, -1);
			Persistance.player.resetDungeonTeam();
		}
	}

	private void processSpawnEvent(PokemonSpawnedEvent event)
	{
		DungeonPokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.register(event.spawned);
		if (event.spawned.player() != null)
		{
			if (event.spawned.player() != Persistance.player)
			{
				if (event.spawned.isTeamLeader()) renderer.sprite().setShadowColor(PokemonSprite.PLAYER_SHADOW);
				else renderer.sprite().setShadowColor(PokemonSprite.ENEMY_SHADOW);
			} else renderer.sprite().setShadowColor(PokemonSprite.ALLY_SHADOW);
		}
	}

	private void processSpeedEvent(SpeedChangedEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.getRenderer(event.pokemon).sprite().updateTickingSpeed(event.pokemon);
	}

	private void processStairEvent(StairLandingEvent event)
	{
		this.setState(State.ANIMATING);
		Persistance.stateManager.setState(new StairMenuState());
	}

	private void processStatEvent(StatChangedEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getStatChangeAnimation(event, this.currentAnimEnd);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processStatusEvent(StatusConditionCreatedEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		AnimationEndListener end = new AnimationEndListener() {
			@Override
			public void onAnimationEnd(AbstractAnimation animation)
			{
				if (animation != null) currentAnimEnd.onAnimationEnd(animation);
				AbstractAnimation a = Animations.getStatusAnimation(event.condition.pokemon, event.condition.condition, null);
				if (a != null)
				{
					a.source = event.condition;
					a.start();
				}
			}
		};
		s.animation = Animations.getCustomAnimation(event.condition.pokemon, 200 + event.condition.condition.id, end);
		if (s.animation == null) end.onAnimationEnd(null);
		else
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processStatusEvent(StatusConditionEndedEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.getRenderer(event.condition.pokemon).removeAnimation(event.condition);
	}

	private void processTravelEvent(PokemonTravelsEvent event)
	{
		this.setState(State.ANIMATING);
		Persistance.dungeonState.setSubstate(new PokemonTravelState(Persistance.dungeonState, event.travels()));
		for (PokemonTravelEvent e : event.travels())
		{
			if (e.pokemon == Persistance.player.getDungeonLeader() && e.destination.type() == TileType.STAIR) this.landedOnStairs = true;
			if (e.pokemon == Persistance.dungeonState.getCameraPokemon()) Persistance.dungeonState.floorVisibility.onCameraMoved();
		}
	}

	private void processWeatherEvent(WeatherChangedEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		if (event.next.weather == Weather.RAIN)
		{
			a.animation = new RainAnimation(100, this.currentAnimEnd);
			a.animation.sound = "weather-rain";
		} else if (event.next.weather == Weather.SNOW)
		{
			a.animation = new SnowAnimation(this.currentAnimEnd);
			a.animation.sound = "weather-snow";
		} else if (event.next.weather == Weather.HAIL)
		{
			a.animation = new RainAnimation(103, this.currentAnimEnd);
			a.animation.sound = "weather-hail";
		} else if (event.next.weather == Weather.SUNNY)
		{
			a.animation = Animations.getCustomAnimation(null, 101, this.currentAnimEnd);
			a.animation.sound = "weather-sunny";
		}
		if (a.animation != null)
		{
			Persistance.dungeonState.setSubstate(a);
			this.setState(State.ANIMATING);
		}
	}

	@Override
	protected void setState(State state)
	{
		super.setState(state);
		if (state == State.AWATING_INPUT && this.dungeon.getActor() == Persistance.player.getDungeonLeader())
			Persistance.dungeonState.setSubstate(Persistance.dungeonState.actionSelectionState);
	}

	@Override
	public boolean stopsTravel(DungeonEvent event)
	{
		return !(event instanceof PokemonTravelsEvent) && super.stopsTravel(event);
	}

}

package com.darkxell.client.mechanics.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.misc.PokemonFaintAnimation;
import com.darkxell.client.mechanics.animation.misc.RainAnimation;
import com.darkxell.client.mechanics.animation.misc.SnowAnimation;
import com.darkxell.client.mechanics.animation.misc.TextAbovePokeAnimation;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.FontMode;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.dialog.ConfirmDialogScreen;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.client.state.dungeon.DungeonExitAnimationState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.dungeon.BlowbackAnimationState;
import com.darkxell.client.state.dungeon.PokemonTravelState;
import com.darkxell.client.state.dungeon.ProjectileAnimationState;
import com.darkxell.client.state.dungeon.ProjectileAnimationState.ProjectileMovement;
import com.darkxell.client.state.menu.dungeon.MoveLearnMenuState;
import com.darkxell.client.state.menu.dungeon.StairMenuState;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.action.PokemonSpawnedEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.dungeon.BossDefeatedEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.ExplorationStopEvent;
import com.darkxell.common.event.dungeon.MissionClearedEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.event.item.MoneyCollectedEvent;
import com.darkxell.common.event.item.ProjectileThrownEvent;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.move.MoveLearnedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.PokemonRescuedEvent;
import com.darkxell.common.event.pokemon.PokemonTeleportedEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.SwitchedPokemonEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.event.stats.LevelupEvent;
import com.darkxell.common.event.stats.SpeedChangedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.effects.FoodItemEffect;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.pokemon.BaseStats;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.status.conditions.ChargedMoveStatusCondition;
import com.darkxell.common.status.conditions.StoreDamageToDoubleStatusCondition;
import com.darkxell.common.util.Direction;
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
			Persistance.eventProcessor().animateDelayed();
		}
	};
	/** Pending events to process. */
	public static final AnimationEndListener processEventsOnAnimationEnd = new AnimationEndListener() {
		@Override
		public void onAnimationEnd(AbstractAnimation animation)
		{
			Persistance.eventProcessor().processPending();
		}
	};

	public static final DialogEndListener processEventsOnDialogEnd = new DialogEndListener() {
		@Override
		public void onDialogEnd(DialogState dialog)
		{
			Persistance.stateManager.setState(Persistance.dungeonState);
			Persistance.eventProcessor().processPending();
		}
	};

	private AnimationEndListener currentAnimEnd = processEventsOnAnimationEnd;
	/** Stores events that animate at the same time as the travel events. */
	private Stack<DungeonEvent> delayedWithTravels = new Stack<>();
	private boolean landedOnStairs = false;
	/** Used to calculate delay time between Pokemon turns. */
	private DungeonEvent lastAction = null;
	private BaseStats levelupStats = null;
	/** Stores consecutive travel events to animate them at the same time. */
	private ArrayList<PokemonTravelEvent> travels = new ArrayList<>();

	public ClientEventProcessor(DungeonExploration dungeon)
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

		if (event instanceof MessageEvent && ((MessageEvent) event).target != null) event.displayMessages = ((MessageEvent) event).target == Persistance.player;

		if (event.displayMessages) Persistance.dungeonState.logger.showMessages(event.getMessages());
		Logger.event(event.loggerMessage());

		if (event instanceof MoveSelectionEvent) this.processMoveEvent((MoveSelectionEvent) event);
		if (event instanceof MoveUseEvent) this.processMoveUseEvent((MoveUseEvent) event);
		if (event instanceof DamageDealtEvent) this.processDamageEvent((DamageDealtEvent) event);
		if (event instanceof HealthRestoredEvent) this.processHealEvent((HealthRestoredEvent) event);
		if (event instanceof StatusConditionCreatedEvent) this.processStatusEvent((StatusConditionCreatedEvent) event);
		if (event instanceof StatusConditionEndedEvent) this.processStatusEvent((StatusConditionEndedEvent) event);

		if (event instanceof PokemonRescuedEvent) this.processRescuedEvent((PokemonRescuedEvent) event);
		if (event instanceof PokemonSpawnedEvent) this.processSpawnEvent((PokemonSpawnedEvent) event);
		if (event instanceof PokemonTravelsEvent) this.processTravelEvent((PokemonTravelsEvent) event);
		if (event instanceof PokemonTeleportedEvent) this.processTeleportEvent((PokemonTeleportedEvent) event);
		if (event instanceof SwitchedPokemonEvent) this.processSwitchEvent((SwitchedPokemonEvent) event);
		if (event instanceof BlowbackPokemonEvent) this.processBlowbackEvent((BlowbackPokemonEvent) event);
		if (event instanceof TurnSkippedEvent) this.processSkipEvent((TurnSkippedEvent) event);
		if (event instanceof FaintedPokemonEvent) this.processFaintedEvent((FaintedPokemonEvent) event);

		if (event instanceof StatChangedEvent) this.processStatEvent((StatChangedEvent) event);
		if (event instanceof SpeedChangedEvent) this.processSpeedEvent((SpeedChangedEvent) event);
		if (event instanceof TriggeredAbilityEvent) this.processAbilityEvent((TriggeredAbilityEvent) event);
		if (event instanceof ExperienceGeneratedEvent) this.processExperienceEvent((ExperienceGeneratedEvent) event);
		if (event instanceof LevelupEvent) this.processLevelupEvent((LevelupEvent) event);
		if (event instanceof MoveDiscoveredEvent) this.processMoveDiscoveredEvent((MoveDiscoveredEvent) event);
		if (event instanceof MoveLearnedEvent) this.processMoveLearnedEvent((MoveLearnedEvent) event);

		if (event instanceof ItemSelectionEvent) this.processItemEvent((ItemSelectionEvent) event);
		if (event instanceof ItemMovedEvent) this.processItemMovedEvent((ItemMovedEvent) event);
		if (event instanceof ItemSwappedEvent) this.processItemSwappedEvent((ItemSwappedEvent) event);
		if (event instanceof MoneyCollectedEvent && Persistance.player.isAlly(((MoneyCollectedEvent) event).pokemon)) SoundManager.playSound("dungeon-money");
		if (event instanceof ProjectileThrownEvent) this.processProjectileEvent((ProjectileThrownEvent) event);

		if (event instanceof WeatherChangedEvent) this.processWeatherEvent((WeatherChangedEvent) event);
		if (event instanceof StairLandingEvent) this.processStairEvent((StairLandingEvent) event);
		if (event instanceof NextFloorEvent) this.processFloorEvent((NextFloorEvent) event);
		if (event instanceof MissionClearedEvent) this.processMissionEvent((MissionClearedEvent) event);
		if (event instanceof DungeonExitEvent) this.processExitEvent((DungeonExitEvent) event);
		if (event instanceof ExplorationStopEvent) this.processExplorationStopEvent((ExplorationStopEvent) event);

		if (this.state() == State.DELAYED) this.animateDelayed();
	}

	@Override
	public void doProcess(DungeonEvent event)
	{
		super.doProcess(event);
		if (this.shouldDelay(event)) this.lastAction = event;
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

		if (this.lastAction != null && this.shouldDelay(event))
		{
			this.addToPending(event);
			Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, 40));
			this.setState(State.ANIMATING);
			this.lastAction = null;
			return false;
		}

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
			if (s.animation.needsPause())
			{
				Persistance.dungeonState.setSubstate(s);
				this.setState(State.ANIMATING);
			} else s.animation.start();
		}
	}

	private void processBlowbackEvent(BlowbackPokemonEvent event)
	{
		Persistance.dungeonState.setSubstate(new BlowbackAnimationState(Persistance.dungeonState, event, this.currentAnimEnd));
		this.setState(State.ANIMATING);
	}

	@Override
	protected void processBossDefeatedEvent(BossDefeatedEvent event)
	{
		super.processBossDefeatedEvent(event);
		if (Persistance.floor.cutsceneOut != null)
		{
			CutsceneManager.playCutscene(Persistance.floor.cutsceneOut);
			this.setState(State.ANIMATING);
		}
	}

	private void processDamageEvent(DamageDealtEvent event)
	{
		if (!(event.source instanceof BellyChangedEvent))
		{
			AnimationState s = new AnimationState(Persistance.dungeonState);
			s.animation = Animations.getCustomAnimation(event.target, Animations.HURT, this.currentAnimEnd);
			if (s.animation != null)
			{
				Persistance.dungeonState.setSubstate(s);
				this.setState(State.ANIMATING);
			}
			if (event.damage != 0) new TextAbovePokeAnimation(event.target, new Message("-" + event.damage, false), FontMode.DAMAGE).start();
		}
	}

	private void processExitEvent(DungeonExitEvent event)
	{
		if (event.player() == Persistance.player) Persistance.dungeonState.setCamera(null);
		Persistance.dungeonState.setSubstate(new DungeonExitAnimationState(Persistance.dungeonState, event.player().getDungeonTeam()));
		this.setState(State.ANIMATING);
	}

	private void processExperienceEvent(ExperienceGeneratedEvent event)
	{
		if (event.experience != 0 && event.player == Persistance.player) for (DungeonPokemon p : event.player.getDungeonTeam())
			if (!p.isFainted()) new TextAbovePokeAnimation(p, new Message("+" + event.experience, false), FontMode.EXPERIENCE).start();
	}

	private void processExplorationStopEvent(ExplorationStopEvent event)
	{
		StateManager.onDungeonEnd(event.outcome);
		Persistance.soundmanager.setBackgroundMusic(null);
	}

	private void processFaintedEvent(FaintedPokemonEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = new PokemonFaintAnimation(event.pokemon, this.currentAnimEnd);
		Persistance.dungeonState.setSubstate(s);
		this.setState(State.ANIMATING);
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
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getCustomAnimation(event.target, Animations.HEAL, this.currentAnimEnd);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
		new TextAbovePokeAnimation(event.target, new Message("+" + event.effectiveHeal(), false), FontMode.DAMAGE).start();
	}

	private void processItemEvent(ItemSelectionEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		if (event.item().effect() instanceof FoodItemEffect && !Animations.existsItemAnimation(event.item()))
			a.animation = Animations.getCustomAnimation(event.target() == null ? event.user() : event.target(), 0, this.currentAnimEnd);
		else a.animation = Animations.getItemAnimation(event.target() == null ? event.user() : event.target(), event.item(), this.currentAnimEnd);
		if (a.animation != null)
		{
			Persistance.dungeonState.setSubstate(a);
			this.setState(State.ANIMATING);
		}
	}

	private void processItemMovedEvent(ItemMovedEvent event)
	{
		if (event.source() instanceof Tile)
		{
			boolean ally = true;
			if (event.destination() instanceof Pokemon) ally = Persistance.player.isAlly((Pokemon) event.destination());
			else if (event.destination() instanceof Inventory) ally = Persistance.player.inventory() == (Inventory) event.destination();
			SoundManager.playSound(ally ? "dungeon-item" : "dungeon-enemygrab");
			Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.source());
		}
	}

	private void processItemSwappedEvent(ItemSwappedEvent event)
	{
		if (event.source() instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.source());
		else if (event.destination() instanceof Tile) Persistance.dungeonState.floorVisibility.onItemremoved((Tile) event.destination());
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

			DialogState state = new DialogState(Persistance.dungeonState, processEventsOnDialogEnd, screens.toArray(new DialogScreen[screens.size()]));

			if (firstLevel)
			{
				SoundManager.playSoundOverMusic("game-levelup");
				Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, 60, (DelayState s) -> Persistance.stateManager.setState(state)));
			} else Persistance.stateManager.setState(state);
		}
	}

	private void processMissionEvent(MissionClearedEvent event)
	{
		if (event.mission.owner == Persistance.player)
		{
			DialogEndListener listener = new DialogEndListener() {

				@Override
				public void onDialogEnd(DialogState dialog)
				{
					Persistance.stateManager.setState(Persistance.dungeonState);
					if (((ConfirmDialogScreen) dialog.getScreen(1)).hasConfirmed()) processEvent(new DungeonExitEvent(Persistance.floor, Persistance.player));
					else processPending();
				}
			};

			DialogScreen screen = new DialogScreen(event.mission.clearedMessage());
			ConfirmDialogScreen confirm = new ConfirmDialogScreen(new Message("mission.cleared.exitoption"));
			confirm.id = 1;
			DialogState dialog = new DialogState(Persistance.dungeonState, listener, screen, confirm);
			Persistance.stateManager.setState(dialog);
			this.setState(State.ANIMATING);
		}
	}

	private void processMoveDiscoveredEvent(MoveDiscoveredEvent event)
	{
		if (event.pokemon.moveCount() == 4)
		{
			this.setState(State.ANIMATING);

			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					Persistance.stateManager.setState(new MoveLearnMenuState(Persistance.dungeonState, event.pokemon, event.move));
				}
			};

			Persistance.stateManager.setState(new DialogState(Persistance.dungeonState, listener, new DialogScreen(
					new Message("moves.learned.full").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<move>", event.move.name()))));
		}
	}

	private void processMoveEvent(MoveSelectionEvent event)
	{
		boolean playAnimationLater = Animations.movePlaysForEachTarget(event.usedMove().move.move());
		boolean hasTarget = false;
		for (DungeonEvent e : event.getResultingEvents())
			if (e instanceof MoveUseEvent)
			{
				hasTarget = true;
				break;
			}
		playAnimationLater &= hasTarget;

		if (!playAnimationLater)
		{
			AnimationEndListener listener = this.currentAnimEnd;

			int projid = event.usedMove().move.moveId();
			if (projid >= 0) projid += 1000;
			ProjectileMovement projMovement = Animations.projectileMovement(projid);
			Tile tile = event.usedMove().user.tile();
			Direction facing = event.usedMove().user.facing();
			if (projMovement == ProjectileMovement.STRAIGHT) do
				tile = tile.adjacentTile(facing);
			while (tile.type() != TileType.WALL && tile.type() != TileType.WALL_END);
			else tile = tile.adjacentTile(facing);
			ProjectileAnimationState proj = new ProjectileAnimationState(Persistance.dungeonState, event.usedMove().user.tile(), tile);
			if (Animations.existsProjectileAnimation(projid) && !hasTarget)
			{
				proj.animation = Animations.getProjectileAnimation(event.usedMove().user, projid, listener);
				proj.movement = projMovement;
				listener = new AnimationEndListener() {
					@Override
					public void onAnimationEnd(AbstractAnimation animation)
					{
						Persistance.dungeonState.setSubstate(proj);
					}
				};
			}

			AnimationState s = new AnimationState(Persistance.dungeonState);
			if (Animations.existsMoveAnimation(event.usedMove().move.move()))
				s.animation = Animations.getMoveAnimation(event.usedMove().user, event.usedMove().move.move(), listener);
			if (s.animation != null)
			{
				Persistance.dungeonState.setSubstate(s);
				this.setState(State.ANIMATING);
			} else if (proj.animation != null)
			{
				Persistance.dungeonState.setSubstate(proj);
				this.setState(State.ANIMATING);
			}

		}

		PokemonSprite sprite = Persistance.dungeonState.pokemonRenderer.getSprite(event.usedMove().user);
		if (sprite.defaultState() == PokemonSpriteState.WITHDRAW) sprite.setDefaultState(PokemonSpriteState.IDLE, false);
	}

	private void processMoveLearnedEvent(MoveLearnedEvent event)
	{
		if (Persistance.player.isAlly(event.pokemon))
		{
			this.setState(State.ANIMATING);
			SoundManager.playSound("game-movelearned");
			Persistance.stateManager.setState(new DialogState(Persistance.dungeonState, ClientEventProcessor.processEventsOnDialogEnd, new DialogScreen(
					new Message("moves.learned").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<move>", event.move.name()))));
		}
	}

	private void processMoveUseEvent(MoveUseEvent event)
	{
		AnimationEndListener listener = this.currentAnimEnd;

		if (event.missed())
		{
			listener = new AnimationEndListener() {
				@Override
				public void onAnimationEnd(AbstractAnimation animation)
				{
					new TextAbovePokeAnimation(event.target, new Message("move.missed"), FontMode.DUNGEON).start();
					currentAnimEnd.onAnimationEnd(animation);
				}
			};
		}

		boolean targetAnim = false;

		AnimationState s = new AnimationState(Persistance.dungeonState);
		if (Animations.existsTargetAnimation(event.usedMove.move.move()))
			s.animation = Animations.getMoveTargetAnimation(event.target, event.usedMove.move.move(), listener);
		if (s.animation != null)
		{
			listener = new AnimationEndListener() {
				@Override
				public void onAnimationEnd(AbstractAnimation animation)
				{
					Persistance.dungeonState.setSubstate(s);
				}
			};
			targetAnim = true;
		}

		boolean projAnim = false;

		ProjectileAnimationState proj = new ProjectileAnimationState(Persistance.dungeonState, event.usedMove.user.tile(),
				event.target == null ? event.usedMove.user.tile() : event.target.tile());
		int projid = event.usedMove.move.moveId();
		if (projid >= 0) projid += 1000;
		if (Animations.existsProjectileAnimation(projid))
		{
			proj.animation = Animations.getProjectileAnimation(event.usedMove.user, projid, listener);
			proj.movement = Animations.projectileMovement(projid);
			listener = new AnimationEndListener() {
				@Override
				public void onAnimationEnd(AbstractAnimation animation)
				{
					Persistance.dungeonState.setSubstate(proj);
				}
			};
			projAnim = true;
		}

		boolean moveAnim = false;

		AnimationState move = new AnimationState(Persistance.dungeonState);
		if (Animations.movePlaysForEachTarget(event.usedMove.move.move()) && Animations.existsMoveAnimation(event.usedMove.move.move()))
			move.animation = Animations.getMoveAnimation(event.usedMove.user, event.usedMove.move.move(), listener);
		if (move.animation != null)
		{
			Persistance.dungeonState.setSubstate(move);
			this.setState(State.ANIMATING);
			moveAnim = true;
		}

		if (!moveAnim)
		{
			if (projAnim)
			{
				Persistance.dungeonState.setSubstate(proj);
				this.setState(State.ANIMATING);
			} else if (targetAnim)
			{
				Persistance.dungeonState.setSubstate(s);
				this.setState(State.ANIMATING);
			} else if (event.missed()) new TextAbovePokeAnimation(event.target, new Message("move.missed"), FontMode.DUNGEON).start();
		}
	}

	private void processProjectileEvent(ProjectileThrownEvent event)
	{
		Item item = event.item;
		if (Animations.existsProjectileAnimation(item.id) && event.target != null)
		{
			Tile destination = event.target == null ? event.thrower.tile().adjacentTile(event.thrower.facing()) : event.target.tile();
			ProjectileAnimationState a = new ProjectileAnimationState(Persistance.dungeonState, event.thrower.tile(), destination);
			a.movement = Animations.projectileMovement(item.id);
			a.animation = Animations.getProjectileAnimation(event.thrower, item.id, this.currentAnimEnd);
			if (a.animation != null)
			{
				Persistance.dungeonState.setSubstate(a);
				this.setState(State.ANIMATING);
			}
		}
	}

	private void processRescuedEvent(PokemonRescuedEvent event)
	{
		Persistance.dungeonState.setSubstate(new DungeonExitAnimationState(Persistance.dungeonState, event.rescued()));
		this.setState(State.ANIMATING);
	}

	private void processSkipEvent(TurnSkippedEvent event)
	{
		if (event.actor() == Persistance.player.getDungeonLeader())
		{
			boolean pause = false;
			if (event.actor().hasStatusCondition(StatusConditions.Asleep))
			{
				Persistance.dungeonState.logger.showMessage(new Message("status.tick.sleep").addReplacement("<pokemon>", event.actor().getNickname()));
				pause = true;
			} else if (event.actor().hasStatusCondition(StatusConditions.Bide))
			{
				Persistance.dungeonState.logger.showMessage(new Message("status.tick.bide").addReplacement("<pokemon>", event.actor().getNickname()));
				pause = true;
			}
			if (pause)
			{
				Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, 40));
				this.setState(State.ANIMATING);
			}
		}
	}

	private void processSpawnEvent(PokemonSpawnedEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.register(event.spawned);
	}

	private void processSpeedEvent(SpeedChangedEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.getRenderer(event.pokemon).sprite().updateTickingSpeed(event.pokemon);
		if (!event.pokemon.stats.hasAStatDown())
		{
			DungeonPokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.getRenderer(event.pokemon);
			if (renderer.hasAnimation(event.pokemon.stats)) renderer.removeAnimation(event.pokemon.stats);
		}
	}

	private void processStairEvent(StairLandingEvent event)
	{
		this.setState(State.ANIMATING);
		Persistance.stateManager.setState(new StairMenuState());
	}

	private void processStatEvent(StatChangedEvent event)
	{
		if (event.effectiveChange() == 0) return;

		AnimationEndListener listener = this.currentAnimEnd;
		if (event.effectiveChange() != 0) listener = new AnimationEndListener() {
			@Override
			public void onAnimationEnd(AbstractAnimation animation)
			{
				DungeonPokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.getRenderer(event.target);
				boolean hasDown = event.target.stats.hasAStatDown();
				if (hasDown && !renderer.hasAnimation(event.target.stats))
				{
					PokemonAnimation a = Animations.getCustomAnimation(event.target, 19, null);
					a.plays = -1;
					a.source = event.target.stats;
					a.start();
				} else if (!hasDown && renderer.hasAnimation(event.target.stats)) renderer.removeAnimation(event.target.stats);
				currentAnimEnd.onAnimationEnd(animation);
			}
		};

		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getStatChangeAnimation(event, listener);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processStatusEvent(StatusConditionCreatedEvent event)
	{
		if (!event.succeeded()) return;
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

		PokemonSprite sprite = Persistance.dungeonState.pokemonRenderer.getSprite(event.condition.pokemon);
		if (event.condition.condition == StatusConditions.Asleep) sprite.setDefaultState(PokemonSpriteState.SLEEP, true);
		else if (event.condition.condition == StatusConditions.Petrified) sprite.setAnimated(false);
		else if (event.condition.condition instanceof StoreDamageToDoubleStatusCondition || event.condition.condition instanceof ChargedMoveStatusCondition)
			sprite.setDefaultState(PokemonSpriteState.WITHDRAW, true);
	}

	private void processStatusEvent(StatusConditionEndedEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.getRenderer(event.condition.pokemon).removeAnimation(event.condition);
		PokemonSprite sprite = Persistance.dungeonState.pokemonRenderer.getSprite(event.condition.pokemon);
		if (!event.condition.pokemon.hasStatusCondition(StatusConditions.Asleep) && sprite.defaultState() == PokemonSpriteState.SLEEP)
			sprite.setDefaultState(PokemonSpriteState.IDLE, false);
		if (!event.condition.pokemon.hasStatusCondition(StatusConditions.Petrified) && !sprite.isAnimated()) sprite.setAnimated(true);
	}

	private void processSwitchEvent(SwitchedPokemonEvent event)
	{
		DungeonPokemonRenderer r = Persistance.dungeonState.pokemonRenderer.getRenderer(event.switcher);
		r.setXY(event.switcher.tile().x, event.switcher.tile().y);
		r = Persistance.dungeonState.pokemonRenderer.getRenderer(event.target);
		r.setXY(event.target.tile().x, event.target.tile().y);

		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getCustomAnimation(event.target, Animations.TELEPORT, this.currentAnimEnd);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processTeleportEvent(PokemonTeleportedEvent event)
	{
		AnimationEndListener listener = new AnimationEndListener() {
			@Override
			public void onAnimationEnd(AbstractAnimation animation)
			{
				Persistance.dungeonState.pokemonRenderer.getRenderer(event.pokemon).setXY(event.destination.x, event.destination.y);
				currentAnimEnd.onAnimationEnd(animation);
			}
		};

		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = Animations.getCustomAnimation(event.pokemon, Animations.TELEPORT, listener);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			this.setState(State.ANIMATING);
		}
	}

	private void processTravelEvent(PokemonTravelsEvent event)
	{
		this.setState(State.ANIMATING);
		Persistance.dungeonState.setSubstate(new PokemonTravelState(Persistance.dungeonState, event.travels()));
		for (PokemonTravelEvent e : event.travels())
		{
			if (e.pokemon() == Persistance.player.getDungeonLeader() && e.destination().type() == TileType.STAIR) this.landedOnStairs = true;
			if (e.pokemon() == Persistance.dungeonState.getCameraPokemon()) Persistance.dungeonState.floorVisibility.onCameraMoved();
		}
	}

	private void processWeatherEvent(WeatherChangedEvent event)
	{
		AnimationState a = new AnimationState(Persistance.dungeonState);
		if (event.next.weather == Weather.RAIN) a.animation = new RainAnimation(100, "weather-rain", this.currentAnimEnd);
		else if (event.next.weather == Weather.SNOW) a.animation = new SnowAnimation(this.currentAnimEnd);
		else if (event.next.weather == Weather.HAIL) a.animation = new RainAnimation(103, "weather-hail", this.currentAnimEnd);
		else if (event.next.weather == Weather.SUNNY) a.animation = Animations.getCustomAnimation(null, 101, this.currentAnimEnd);
		if (a.animation != null)
		{
			Persistance.dungeonState.staticAnimationsRenderer.add(a.animation);
			Persistance.dungeonState.setSubstate(a);
			this.setState(State.ANIMATING);
		}
	}

	@Override
	public void setState(State state)
	{
		super.setState(state);
		if (state == State.AWATING_INPUT)
		{
			this.lastAction = null; // Don't delay if game was already waiting for a Player's action.
			if (this.dungeon.getActor() == Persistance.player.getDungeonLeader())
				Persistance.dungeonState.setSubstate(Persistance.dungeonState.actionSelectionState);
		}
	}

	/** @return <code>true</code> If having the input event and another <i>shouldDelay</i> event should delay the game for a few ticks, to give the Player a break. */
	protected boolean shouldDelay(DungeonEvent event)
	{
		if (event.actor() == null) return false;
		return (event instanceof MoveSelectionEvent) || (event instanceof ItemSelectionEvent) || (event instanceof ItemSwappedEvent)
				|| (event instanceof ItemMovedEvent);
	}

	@Override
	public boolean stopsTravel(DungeonEvent event)
	{
		return !(event instanceof PokemonTravelsEvent) && super.stopsTravel(event);
	}

}

package com.darkxell.client.mechanics.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.graphics.TextRenderer.FontMode;
import com.darkxell.client.graphics.renderer.DungeonPokemonRenderer;
import com.darkxell.client.graphics.renderer.OnFirstPokemonDraw;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.misc.PokemonFaintAnimation;
import com.darkxell.client.mechanics.animation.misc.RainAnimation;
import com.darkxell.client.mechanics.animation.misc.SnowAnimation;
import com.darkxell.client.mechanics.animation.misc.TextAbovePokeAnimation;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.dialog.ConfirmDialogScreen;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.BlowbackAnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.client.state.dungeon.DungeonExitAnimationState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.dungeon.PokemonTravelState;
import com.darkxell.client.state.dungeon.ProjectileAnimationState;
import com.darkxell.client.state.dungeon.ProjectileAnimationState.ProjectileMovement;
import com.darkxell.client.state.menu.dungeon.MoveLearnMenuState;
import com.darkxell.client.state.menu.dungeon.StairMenuState;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.floor.Floor;
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
import com.darkxell.common.event.dungeon.TrapSteppedOnEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.event.item.ItemLandedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.event.item.ItemThrownEvent;
import com.darkxell.common.event.item.MoneyCollectedEvent;
import com.darkxell.common.event.item.ProjectileThrownEvent;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.move.MoveLearnedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.PokemonRescuedEvent;
import com.darkxell.common.event.pokemon.PokemonTeleportedEvent;
import com.darkxell.common.event.pokemon.RevivedPokemonEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.SwitchedPokemonEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.event.stats.LevelupEvent;
import com.darkxell.common.event.stats.SpeedChangedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.effects.FoodItemEffect;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.pokemon.BaseStats;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.status.conditions.ChargedMoveStatusCondition;
import com.darkxell.common.status.conditions.StoreDamageToDoubleStatusCondition;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

/**
 * Translates game logic events into displayable content to the client.<br />
 * Takes in Events to display messages, manage resources or change game states.
 */
public final class ClientEventProcessor extends CommonEventProcessor {
    public static final AnimationEndListener animateDelayedOnAnimationEnd = animation -> Persistence.eventProcessor()
            .animateDelayed();
    /** Pending events to process. */
    public static final AnimationEndListener processEventsOnAnimationEnd = animation -> Persistence.eventProcessor()
            .processPending();

    public static final DialogEndListener processEventsOnDialogEnd = dialog -> {
        Persistence.stateManager.setState(Persistence.dungeonState);
        Persistence.eventProcessor().processPending();
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

    public ClientEventProcessor(DungeonExploration dungeon) {
        super(dungeon);
    }

    public void animateDelayed() {
        this.setState(State.DELAYED);
        if (!this.travels.isEmpty()) {
            this.currentAnimEnd = animateDelayedOnAnimationEnd;
            PokemonTravelsEvent e = new PokemonTravelsEvent(this.dungeon.currentFloor(), this.travels);
            this.travels.clear();
            this.doClientProcess(e);
        } else if (!this.delayedWithTravels.isEmpty())
            this.doClientProcess(this.delayedWithTravels.pop());
        else {
            this.setState(State.PROCESSING);
            this.currentAnimEnd = processEventsOnAnimationEnd;
            this.processPending();
        }
    }

    private void doClientProcess(DungeonEvent event) {
        if (this.delayedWithTravels.contains(event))
            return;

        if (event instanceof MessageEvent && ((MessageEvent) event).target != null)
            event.displayMessages = ((MessageEvent) event).target == Persistence.player;

        if (event.displayMessages)
            Persistence.dungeonState.logger.showMessages(event.getMessages());
        Logger.event(event.loggerMessage());

        if (event instanceof MoveSelectionEvent)
            this.processMoveEvent((MoveSelectionEvent) event);
        if (event instanceof MoveUseEvent)
            this.processMoveUseEvent((MoveUseEvent) event);
        if (event instanceof DamageDealtEvent)
            this.processDamageEvent((DamageDealtEvent) event);
        if (event instanceof HealthRestoredEvent)
            this.processHealEvent((HealthRestoredEvent) event);
        if (event instanceof StatusConditionCreatedEvent)
            this.processStatusEvent((StatusConditionCreatedEvent) event);
        if (event instanceof StatusConditionEndedEvent)
            this.processStatusEvent((StatusConditionEndedEvent) event);

        if (event instanceof PokemonRescuedEvent)
            this.processRescuedEvent((PokemonRescuedEvent) event);
        if (event instanceof PokemonSpawnedEvent)
            this.processSpawnEvent((PokemonSpawnedEvent) event);
        if (event instanceof PokemonTravelsEvent)
            this.processTravelEvent((PokemonTravelsEvent) event);
        if (event instanceof PokemonTeleportedEvent)
            this.processTeleportEvent((PokemonTeleportedEvent) event);
        if (event instanceof SwitchedPokemonEvent)
            this.processSwitchEvent((SwitchedPokemonEvent) event);
        if (event instanceof BlowbackPokemonEvent)
            this.processBlowbackEvent((BlowbackPokemonEvent) event);
        if (event instanceof TurnSkippedEvent)
            this.processSkipEvent((TurnSkippedEvent) event);
        if (event instanceof FaintedPokemonEvent)
            this.processFaintedEvent((FaintedPokemonEvent) event);
        if (event instanceof RevivedPokemonEvent)
            this.processRevivedEvent((RevivedPokemonEvent) event);

        if (event instanceof StatChangedEvent)
            this.processStatEvent((StatChangedEvent) event);
        if (event instanceof SpeedChangedEvent)
            this.processSpeedEvent((SpeedChangedEvent) event);
        if (event instanceof TriggeredAbilityEvent)
            this.processAbilityEvent((TriggeredAbilityEvent) event);
        if (event instanceof ExperienceGeneratedEvent)
            this.processExperienceEvent((ExperienceGeneratedEvent) event);
        if (event instanceof LevelupEvent)
            this.processLevelupEvent((LevelupEvent) event);
        if (event instanceof MoveDiscoveredEvent)
            this.processMoveDiscoveredEvent((MoveDiscoveredEvent) event);
        if (event instanceof MoveLearnedEvent)
            this.processMoveLearnedEvent((MoveLearnedEvent) event);

        if (event instanceof ItemSelectionEvent)
            this.processItemEvent((ItemSelectionEvent) event);
        if (event instanceof ItemMovedEvent)
            this.processItemMovedEvent((ItemMovedEvent) event);
        if (event instanceof ItemSwappedEvent)
            this.processItemSwappedEvent((ItemSwappedEvent) event);
        if (event instanceof MoneyCollectedEvent && Persistence.player.isAlly(((MoneyCollectedEvent) event).pokemon))
            SoundManager.playSound("dungeon-money");
        if (event instanceof ItemThrownEvent)
            this.processItemThrownEvent((ItemThrownEvent) event);
        if (event instanceof ProjectileThrownEvent)
            this.processProjectileEvent((ProjectileThrownEvent) event);
        if (event instanceof ItemLandedEvent)
            this.processItemLandedEvent((ItemLandedEvent) event);

        if (event instanceof WeatherChangedEvent)
            this.processWeatherEvent((WeatherChangedEvent) event);
        if (event instanceof TrapSteppedOnEvent)
            this.processTrapEvent((TrapSteppedOnEvent) event);
        if (event instanceof StairLandingEvent)
            this.processStairEvent((StairLandingEvent) event);
        if (event instanceof NextFloorEvent)
            this.processFloorEvent((NextFloorEvent) event);
        if (event instanceof MissionClearedEvent)
            this.processMissionEvent((MissionClearedEvent) event);
        if (event instanceof DungeonExitEvent)
            this.processExitEvent((DungeonExitEvent) event);
        if (event instanceof ExplorationStopEvent)
            this.processExplorationStopEvent((ExplorationStopEvent) event);

        if (this.state() == State.DELAYED)
            this.animateDelayed();
    }

    @Override
    public void doProcess(DungeonEvent event) {
        super.doProcess(event);
        if (this.shouldDelay(event))
            this.lastAction = event;
        this.doClientProcess(event);
    }

    @Override
    public void onFloorStart(Floor floor) {
        super.onFloorStart(floor);
        OnFirstPokemonDraw.reset();
    }

    @Override
    public void onTurnEnd() {
        if (!this.travels.isEmpty())
            this.animateDelayed();
        else {
            if (this.landedOnStairs) {
                this.addToPending(new StairLandingEvent());
                this.landedOnStairs = false;
            }
            OnFirstPokemonDraw.update();
            Logger.event("Turn ended ---------------");
            super.onTurnEnd();
        }
    }

    @Override
    protected boolean preProcess(DungeonEvent event) {

        if (this.lastAction != null && this.shouldDelay(event)) {
            this.addToPending(event);
            Persistence.dungeonState.setSubstate(new DelayState(Persistence.dungeonState, 40));
            this.setState(State.ANIMATING);
            this.lastAction = null;
            return false;
        }

        if (event instanceof PokemonTravelEvent)
            this.travels.add((PokemonTravelEvent) event);
        else if (!this.travels.isEmpty())
            if (this.stopsTravel(event)) {
                this.addToPending(event);
                this.animateDelayed();
                return false;
            } else
                this.delayedWithTravels.push(event);
        return super.preProcess(event);
    }

    private void processAbilityEvent(TriggeredAbilityEvent event) {
        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getAbilityAnimation(event.pokemon, event.ability, this.currentAnimEnd);
        if (s.animation != null)
            if (s.animation.needsPause()) {
                Persistence.dungeonState.setSubstate(s);
                this.setState(State.ANIMATING);
            } else
                s.animation.start();
    }

    private void processBlowbackEvent(BlowbackPokemonEvent event) {
        Persistence.dungeonState
                .setSubstate(new BlowbackAnimationState(Persistence.dungeonState, event, this.currentAnimEnd));
        this.setState(State.ANIMATING);
    }

    @Override
    protected void processBossDefeatedEvent(BossDefeatedEvent event) {
        super.processBossDefeatedEvent(event);
        if (Persistence.floor.cutsceneOut != null && (Persistence.floor.cutsceneStorypos == -1
                || Persistence.floor.cutsceneStorypos == Persistence.player.storyPosition())) {
            CutsceneManager.playCutscene(Persistence.floor.cutsceneOut, false);
            this.setState(State.ANIMATING);
        }
    }

    private void processDamageEvent(DamageDealtEvent event) {
        if (event.damageType != DamageType.HUNGER) {
            AnimationState s = new AnimationState(Persistence.dungeonState);
            s.animation = Animations.getCustomAnimation(event.target, Animations.HURT, this.currentAnimEnd);
            if (s.animation != null) {
                Persistence.dungeonState.setSubstate(s);
                this.setState(State.ANIMATING);
            }
            if (event.damage != 0)
                new TextAbovePokeAnimation(Persistence.dungeonState.pokemonRenderer.getRenderer(event.target),
                        new Message("-" + event.damage, false), FontMode.DAMAGE).start();
        }
    }

    private void processExitEvent(DungeonExitEvent event) {
        if (event.player() == Persistence.player)
            Persistence.dungeonState.setCamera(null);
        Persistence.dungeonState
                .setSubstate(new DungeonExitAnimationState(Persistence.dungeonState, event.player().getDungeonTeam()));
        this.setState(State.ANIMATING);
    }

    private void processExperienceEvent(ExperienceGeneratedEvent event) {
        if (event.experience != 0 && event.player == Persistence.player)
            for (DungeonPokemon p : event.player.getDungeonTeam())
                if (!p.isFainted())
                    new TextAbovePokeAnimation(Persistence.dungeonState.pokemonRenderer.getRenderer(p),
                            new Message("+" + event.experience, false), FontMode.EXPERIENCE).start();
    }

    private void processExplorationStopEvent(ExplorationStopEvent event) {
        StateManager.onDungeonEnd(event.outcome);
        Persistence.soundmanager.setBackgroundMusic(null);
    }

    private void processFaintedEvent(FaintedPokemonEvent event) {
        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = new PokemonFaintAnimation(Persistence.dungeonState.pokemonRenderer.getRenderer(event.pokemon),
                this.currentAnimEnd);
        Persistence.dungeonState.setSubstate(s);
        this.setState(State.ANIMATING);
    }

    private void processFloorEvent(NextFloorEvent event) {
        this.setState(State.ANIMATING);
        this.delayedWithTravels.clear();
        Persistence.stateManager.setState(new NextFloorState(Persistence.dungeonState, event.floor.id + 1));
    }

    private void processHealEvent(HealthRestoredEvent event) {
        if (event.effectiveHeal() <= 0)
            return;
        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getCustomAnimation(event.target, Animations.HEAL, this.currentAnimEnd);
        if (s.animation != null) {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }
        new TextAbovePokeAnimation(Persistence.dungeonState.pokemonRenderer.getRenderer(event.target),
                new Message("+" + event.effectiveHeal(), false), FontMode.DAMAGE).start();
    }

    private void processItemEvent(ItemSelectionEvent event) {
        AnimationState a = new AnimationState(Persistence.dungeonState);
        if (event.item().effect() instanceof FoodItemEffect && !Animations.existsItemAnimation(event.item()))
            a.animation = Animations.getCustomAnimation(event.target() == null ? event.user() : event.target(), 0,
                    this.currentAnimEnd);
        else
            a.animation = Animations.getItemAnimation(event.target() == null ? event.user() : event.target(),
                    event.item(), this.currentAnimEnd);
        if (a.animation != null) {
            Persistence.dungeonState.setSubstate(a);
            this.setState(State.ANIMATING);
        }
    }

    private void processItemLandedEvent(ItemLandedEvent event) {
        if (event.tile != event.destination()) {
            AnimationEndListener listener = animation -> {
                Persistence.dungeonState.itemRenderer.hidden.remove(event.item);
                currentAnimEnd.onAnimationEnd(animation);
            };

            Persistence.dungeonState.itemRenderer.hidden.add(event.item);
            ProjectileAnimationState a = new ProjectileAnimationState(Persistence.dungeonState, event.tile,
                    event.destination());
            a.animation = Animations.getProjectileAnimationFromItem(null, event.item.item(), listener);
            a.movement = ProjectileMovement.ARC;
            if (a.animation != null) {
                Persistence.dungeonState.setSubstate(a);
                this.setState(State.ANIMATING);
            }
        }
    }

    private void processItemMovedEvent(ItemMovedEvent event) {
        if (event.source() instanceof Tile) {
            boolean ally = true;
            if (event.destination() instanceof DungeonPokemon)
                ally = Persistence.player.isAlly((DungeonPokemon) event.destination());
            else if (event.destination() instanceof Inventory)
                ally = Persistence.player.inventory() == (Inventory) event.destination();
            SoundManager.playSound(ally ? "dungeon-item" : "dungeon-enemygrab");
            Persistence.dungeonState.floorVisibility.onItemremoved((Tile) event.source());
        }
    }

    private void processItemSwappedEvent(ItemSwappedEvent event) {
        if (event.source() instanceof Tile)
            Persistence.dungeonState.floorVisibility.onItemremoved((Tile) event.source());
        else if (event.destination() instanceof Tile)
            Persistence.dungeonState.floorVisibility.onItemremoved((Tile) event.destination());
    }

    private void processItemThrownEvent(ItemThrownEvent event) {
        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getCustomAnimation(event.thrower(), Animations.THROW, this.currentAnimEnd);
        if (s.animation != null) {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }
    }

    private void processLevelupEvent(LevelupEvent event) {
        Pokemon pokemon = event.pokemon;
        if (Persistence.player.isAlly(pokemon)) {
            this.setState(State.ANIMATING);
            boolean firstLevel = this.levelupStats == null;

            if (this.levelupStats == null)
                this.levelupStats = pokemon.species().baseStatsIncrease(pokemon.level() - 1);
            else
                this.levelupStats.add(pokemon.species().baseStatsIncrease(pokemon.level() - 1));

            ArrayList<DialogScreen> screens = new ArrayList<DialogScreen>();
            screens.add(new DialogScreen(new Message("xp.levelup").addReplacement("<pokemon>", pokemon.getNickname())
                    .addReplacement("<level>", Integer.toString(pokemon.level()))));

            {
                boolean hasMoreLevels = false;
                for (DungeonEvent e : this.pending)
                    if (e instanceof LevelupEvent && ((LevelupEvent) e).pokemon == pokemon) {
                        hasMoreLevels = true;
                        break;
                    }
                if (!hasMoreLevels) {
                    screens.add(new DialogScreen(new Message("xp.stats")
                            .addReplacement("<atk>", TextRenderer.alignNumber(this.levelupStats.getAttack(), 2))
                            .addReplacement("<def>", TextRenderer.alignNumber(this.levelupStats.getDefense(), 2))
                            .addReplacement("<hea>", TextRenderer.alignNumber(this.levelupStats.getHealth(), 2))
                            .addReplacement("<spa>", TextRenderer.alignNumber(this.levelupStats.getSpecialAttack(), 2))
                            .addReplacement("<spd>",
                                    TextRenderer.alignNumber(this.levelupStats.getSpecialDefense(), 2))));
                    this.levelupStats = null;
                }
            }

            DialogState state = new DialogState(Persistence.dungeonState, processEventsOnDialogEnd,
                    screens.toArray(new DialogScreen[screens.size()]));

            if (firstLevel) {
                SoundManager.playSoundOverMusic("game-levelup");
                Persistence.dungeonState.setSubstate(new DelayState(Persistence.dungeonState, 60,
                        (DelayState s) -> Persistence.stateManager.setState(state)));
            } else
                Persistence.stateManager.setState(state);
        }
    }

    private void processMissionEvent(MissionClearedEvent event) {
        if (event.mission.owner == Persistence.player) {
            DialogEndListener listener = dialog -> {
                Persistence.stateManager.setState(Persistence.dungeonState);
                if (((ConfirmDialogScreen) dialog.getScreen(1)).hasConfirmed())
                    processEvent(new DungeonExitEvent(Persistence.floor, Persistence.player));
                else
                    processPending();
            };

            DialogScreen screen = new DialogScreen(event.mission.clearedMessage());
            ConfirmDialogScreen confirm = new ConfirmDialogScreen(new Message("mission.cleared.exitoption"));
            confirm.id = 1;
            DialogState dialog = new DialogState(Persistence.dungeonState, listener, screen, confirm);
            Persistence.stateManager.setState(dialog);
            this.setState(State.ANIMATING);
        }
    }

    private void processMoveDiscoveredEvent(MoveDiscoveredEvent event) {
        if (event.pokemon.moveCount() == 4) {
            this.setState(State.ANIMATING);

            DialogEndListener listener = dialog -> Persistence.stateManager
                    .setState(new MoveLearnMenuState(Persistence.dungeonState, event.pokemon, event.move));

            Persistence.stateManager
                    .setState(new DialogState(Persistence.dungeonState, listener,
                            new DialogScreen(new Message("moves.learned.full")
                                    .addReplacement("<pokemon>", event.pokemon.getNickname())
                                    .addReplacement("<move>", event.move.name()))));
        }
    }

    private void processMoveEvent(MoveSelectionEvent event) {
        PokemonSprite sprite = Persistence.dungeonState.pokemonRenderer.getRenderer(event.usedMove().user).sprite();
        if (event.usedMove().direction != sprite.getFacingDirection())
            sprite.setFacingDirection(event.usedMove().direction);

        boolean playAnimationLater = Animations.movePlaysForEachTarget(event.usedMove().move.move());
        boolean hasTarget = false;
        for (DungeonEvent e : event.getResultingEvents())
            if (e instanceof MoveUseEvent) {
                hasTarget = true;
                break;
            }
        playAnimationLater &= hasTarget;

        if (!playAnimationLater) {
            AnimationEndListener listener = this.currentAnimEnd;

            int projid = event.usedMove().move.moveId();
            if (projid >= 0)
                projid += 1000;
            ProjectileMovement projMovement = Animations.projectileMovement(projid);
            Tile tile = event.usedMove().user.tile();
            Direction facing = event.usedMove().user.facing();
            if (projMovement == ProjectileMovement.STRAIGHT && (event.usedMove().move.move().range == MoveRange.Line))
                do
                    tile = tile.adjacentTile(facing);
                while (tile.type() != TileType.WALL && tile.type() != TileType.WALL_END);
            else
                tile = tile.adjacentTile(facing);
            ProjectileAnimationState proj = new ProjectileAnimationState(Persistence.dungeonState,
                    event.usedMove().user.tile(), tile);
            if (Animations.existsProjectileAnimation(projid) && !hasTarget) {
                proj.animation = Animations.getProjectileAnimation(event.usedMove().user, projid, listener);
                proj.movement = projMovement;
                listener = animation -> Persistence.dungeonState.setSubstate(proj);
            }

            AnimationState s = new AnimationState(Persistence.dungeonState);
            if (Animations.existsMoveAnimation(event.usedMove().move.move()))
                s.animation = Animations.getMoveAnimation(event.usedMove().user, event.usedMove().move.move(),
                        listener);
            if (s.animation != null) {
                Persistence.dungeonState.setSubstate(s);
                this.setState(State.ANIMATING);
            } else if (proj.animation != null) {
                Persistence.dungeonState.setSubstate(proj);
                this.setState(State.ANIMATING);
            }

        }

        if (sprite.defaultState() == PokemonSpriteState.WITHDRAW)
            sprite.setDefaultState(PokemonSpriteState.IDLE, false);
    }

    private void processMoveLearnedEvent(MoveLearnedEvent event) {
        if (Persistence.player.isAlly(event.pokemon)) {
            this.setState(State.ANIMATING);
            SoundManager.playSound("game-movelearned");
            Persistence.stateManager
                    .setState(new DialogState(Persistence.dungeonState, ClientEventProcessor.processEventsOnDialogEnd,
                            new DialogScreen(new Message("moves.learned")
                                    .addReplacement("<pokemon>", event.pokemon.getNickname())
                                    .addReplacement("<move>", event.move.name()))));
        }
    }

    private void processMoveUseEvent(MoveUseEvent event) {
        AnimationEndListener listener = this.currentAnimEnd;

        if (event.missed())
            listener = animation -> {
                new TextAbovePokeAnimation(Persistence.dungeonState.pokemonRenderer.getRenderer(event.target),
                        new Message("move.missed"), FontMode.DUNGEON).start();
                currentAnimEnd.onAnimationEnd(animation);
            };

        boolean targetAnim = false;

        AnimationState s = new AnimationState(Persistence.dungeonState);
        if (Animations.existsTargetAnimation(event.usedMove.move.move()))
            s.animation = Animations.getMoveTargetAnimation(event.target, event.usedMove.move.move(), listener);
        if (s.animation != null) {
            listener = animation -> Persistence.dungeonState.setSubstate(s);
            targetAnim = true;
        }

        boolean projAnim = false;

        ProjectileAnimationState proj = new ProjectileAnimationState(Persistence.dungeonState,
                event.usedMove.user.tile(), event.target == null ? event.usedMove.user.tile() : event.target.tile());
        int projid = event.usedMove.move.moveId();
        if (projid >= 0)
            projid += 1000;
        if (Animations.existsProjectileAnimation(projid)) {
            proj.animation = Animations.getProjectileAnimation(event.usedMove.user, projid, listener);
            proj.movement = Animations.projectileMovement(projid);
            listener = animation -> Persistence.dungeonState.setSubstate(proj);
            projAnim = true;
        }

        boolean moveAnim = false;

        AnimationState move = new AnimationState(Persistence.dungeonState);
        if (Animations.movePlaysForEachTarget(event.usedMove.move.move())
                && Animations.existsMoveAnimation(event.usedMove.move.move()))
            move.animation = Animations.getMoveAnimation(event.usedMove.user, event.usedMove.move.move(), listener);
        if (move.animation != null) {
            Persistence.dungeonState.setSubstate(move);
            this.setState(State.ANIMATING);
            moveAnim = true;
        }

        if (!moveAnim)
            if (projAnim) {
                Persistence.dungeonState.setSubstate(proj);
                this.setState(State.ANIMATING);
            } else if (targetAnim) {
                Persistence.dungeonState.setSubstate(s);
                this.setState(State.ANIMATING);
            } else if (event.missed())
                new TextAbovePokeAnimation(Persistence.dungeonState.pokemonRenderer.getRenderer(event.target),
                        new Message("move.missed"), FontMode.DUNGEON).start();
    }

    private void processProjectileEvent(ProjectileThrownEvent event) {
        Item item = event.item;
        ProjectileAnimationState a = new ProjectileAnimationState(Persistence.dungeonState, event.thrower.tile(),
                event.destination);
        if (Animations.existsProjectileAnimation(item.id)) {
            a.movement = Animations.projectileMovement(item.id);
            a.animation = Animations.getProjectileAnimation(event.thrower, item.id, this.currentAnimEnd);
        } else
            a.animation = Animations.getProjectileAnimationFromItem(event.thrower, item, this.currentAnimEnd);

        a.shouldBounce = event.destination.isWall();

        if (a.animation != null) {
            Persistence.dungeonState.setSubstate(a);
            this.setState(State.ANIMATING);
        }
    }

    private void processRescuedEvent(PokemonRescuedEvent event) {
        Persistence.dungeonState.setSubstate(new DungeonExitAnimationState(Persistence.dungeonState, event.rescued()));
        this.setState(State.ANIMATING);
    }

    private void processRevivedEvent(RevivedPokemonEvent event) {
        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getCustomAnimation(event.pokemon, Animations.REVIVE, this.currentAnimEnd);
        if (s.animation != null) {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }

        PokemonSprite sprite = Persistence.dungeonState.pokemonRenderer.getSprite(event.pokemon);
        if (sprite.defaultState() == PokemonSpriteState.SLEEP)
            sprite.setDefaultState(PokemonSpriteState.IDLE, false);
        if (!sprite.isAnimated())
            sprite.setAnimated(true);
    }

    private void processSkipEvent(TurnSkippedEvent event) {
        if (event.actor() == Persistence.player.getDungeonLeader()) {
            boolean pause = false;
            if (event.actor().hasStatusCondition(StatusConditions.Asleep)) {
                Persistence.dungeonState.logger.showMessage(
                        new Message("status.tick.sleep").addReplacement("<pokemon>", event.actor().getNickname()));
                pause = true;
            } else if (event.actor().hasStatusCondition(StatusConditions.Bide)) {
                Persistence.dungeonState.logger.showMessage(
                        new Message("status.tick.bide").addReplacement("<pokemon>", event.actor().getNickname()));
                pause = true;
            } else if (event.actor().hasStatusCondition(StatusConditions.Frozen)) {
                Persistence.dungeonState.logger.showMessage(
                        new Message("status.tick.frozen").addReplacement("<pokemon>", event.actor().getNickname()));
                pause = true;
            }
            if (pause) {
                Persistence.dungeonState.setSubstate(new DelayState(Persistence.dungeonState, 40));
                this.setState(State.ANIMATING);
            }
        }
    }

    private void processSpawnEvent(PokemonSpawnedEvent event) {
        Persistence.dungeonState.pokemonRenderer.register(event.spawned);
    }

    private void processSpeedEvent(SpeedChangedEvent event) {
        Persistence.dungeonState.pokemonRenderer.getRenderer(event.pokemon).sprite().updateTickingSpeed(event.pokemon);
        if (!event.pokemon.stats.hasAStatDown()) {
            DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(event.pokemon);
            if (renderer.hasAnimation(event.pokemon.stats))
                renderer.removeAnimation(event.pokemon.stats);
        }
    }

    private void processStairEvent(StairLandingEvent event) {
        this.setState(State.ANIMATING);
        Persistence.stateManager.setState(new StairMenuState());
    }

    private void processStatEvent(StatChangedEvent event) {
        if (event.effectiveChange() == 0)
            return;

        AnimationEndListener listener = this.currentAnimEnd;
        if (event.effectiveChange() != 0)
            listener = animation -> {
                DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(event.target);
                boolean hasDown = event.target.stats.hasAStatDown();
                if (hasDown && !renderer.hasAnimation(event.target.stats)) {
                    PokemonAnimation a = Animations.getCustomAnimation(event.target, 19, null);
                    a.plays = -1;
                    a.source = event.target.stats;
                    a.start();
                } else if (!hasDown && renderer.hasAnimation(event.target.stats))
                    renderer.removeAnimation(event.target.stats);
                currentAnimEnd.onAnimationEnd(animation);
            };

        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getStatChangeAnimation(event, listener);
        if (s.animation != null) {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }
    }

    private void processStatusEvent(StatusConditionCreatedEvent event) {
        if (!event.succeeded())
            return;
        AnimationState s = new AnimationState(Persistence.dungeonState);
        AnimationEndListener end = animation -> {
            if (animation != null)
                currentAnimEnd.onAnimationEnd(animation);
            AbstractAnimation a = Animations.getStatusAnimation(event.condition.pokemon, event.condition.condition,
                    null);
            if (a != null) {
                a.source = event.condition;
                a.start();
            }
        };
        s.animation = Animations.getCustomAnimation(event.condition.pokemon, 200 + event.condition.condition.id, end);
        if (s.animation == null)
            end.onAnimationEnd(null);
        else {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }

        PokemonSprite sprite = Persistence.dungeonState.pokemonRenderer.getSprite(event.condition.pokemon);
        if (event.condition.condition == StatusConditions.Asleep)
            sprite.setDefaultState(PokemonSpriteState.SLEEP, true);
        else if (event.condition.condition == StatusConditions.Frozen)
            sprite.setAnimated(false);
        else if (event.condition.condition == StatusConditions.Petrified)
            sprite.setAnimated(false);
        else if (event.condition.condition instanceof StoreDamageToDoubleStatusCondition
                || event.condition.condition instanceof ChargedMoveStatusCondition)
            sprite.setDefaultState(PokemonSpriteState.WITHDRAW, true);
    }

    private void processStatusEvent(StatusConditionEndedEvent event) {
        Persistence.dungeonState.pokemonRenderer.getRenderer(event.condition.pokemon).removeAnimation(event.condition);
        PokemonSprite sprite = Persistence.dungeonState.pokemonRenderer.getSprite(event.condition.pokemon);
        if (!event.condition.pokemon.hasStatusCondition(StatusConditions.Asleep)
                && sprite.defaultState() == PokemonSpriteState.SLEEP)
            sprite.setDefaultState(PokemonSpriteState.IDLE, false);
        if (!event.condition.pokemon.hasStatusCondition(StatusConditions.Frozen) && !sprite.isAnimated())
            sprite.setAnimated(true);
        else if (!event.condition.pokemon.hasStatusCondition(StatusConditions.Petrified) && !sprite.isAnimated())
            sprite.setAnimated(true);
    }

    private void processSwitchEvent(SwitchedPokemonEvent event) {
        DungeonPokemonRenderer r = Persistence.dungeonState.pokemonRenderer.getRenderer(event.switcher);
        r.setXY(event.switcher.tile().x, event.switcher.tile().y);
        r = Persistence.dungeonState.pokemonRenderer.getRenderer(event.target);
        r.setXY(event.target.tile().x, event.target.tile().y);

        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getCustomAnimation(event.target, Animations.TELEPORT, this.currentAnimEnd);
        if (s.animation != null) {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }
    }

    private void processTeleportEvent(PokemonTeleportedEvent event) {
        AnimationEndListener listener = animation -> {
            Persistence.dungeonState.pokemonRenderer.getRenderer(event.pokemon).setXY(event.destination.x,
                    event.destination.y);
            currentAnimEnd.onAnimationEnd(animation);
        };

        AnimationState s = new AnimationState(Persistence.dungeonState);
        s.animation = Animations.getCustomAnimation(event.pokemon, Animations.TELEPORT, listener);
        if (s.animation != null) {
            Persistence.dungeonState.setSubstate(s);
            this.setState(State.ANIMATING);
        }
    }

    private void processTrapEvent(TrapSteppedOnEvent event) {
        if (event.trap == TrapRegistry.WONDER_TILE) {
            DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(event.pokemon);
            if (!event.pokemon.stats.hasAStatDown() && renderer.hasAnimation(event.pokemon.stats))
                renderer.removeAnimation(event.pokemon.stats);
        }
    }

    private void processTravelEvent(PokemonTravelsEvent event) {
        this.setState(State.ANIMATING);
        Persistence.dungeonState.setSubstate(new PokemonTravelState(Persistence.dungeonState, event.travels()));
        for (PokemonTravelEvent e : event.travels()) {
            if (e.pokemon() == Persistence.player.getDungeonLeader() && e.destination().type() == TileType.STAIR)
                this.landedOnStairs = true;
            if (e.pokemon() == Persistence.dungeonState.getCameraPokemon())
                Persistence.dungeonState.floorVisibility.onCameraMoved();
        }
    }

    private void processWeatherEvent(WeatherChangedEvent event) {
        AnimationState a = new AnimationState(Persistence.dungeonState);
        if (event.next.weather == Weather.RAIN)
            a.animation = new RainAnimation(100, "weather-rain", this.currentAnimEnd);
        else if (event.next.weather == Weather.SNOW)
            a.animation = new SnowAnimation(this.currentAnimEnd);
        else if (event.next.weather == Weather.HAIL)
            a.animation = new RainAnimation(103, "weather-hail", this.currentAnimEnd);
        else if (event.next.weather == Weather.SUNNY)
            a.animation = Animations.getCustomAnimation(null, 101, this.currentAnimEnd);
        if (a.animation != null) {
            Persistence.dungeonState.staticAnimationsRenderer.add(a.animation);
            Persistence.dungeonState.setSubstate(a);
            this.setState(State.ANIMATING);
        }
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        if (state == State.AWATING_INPUT) {
            this.lastAction = null; // Don't delay if game was already waiting for a Player's action.
            if (this.dungeon.getActor() == Persistence.player.getDungeonLeader())
                Persistence.dungeonState.setSubstate(Persistence.dungeonState.actionSelectionState);
        }
    }

    /**
     * @return <code>true</code> If having the input event and another <i>shouldDelay</i> event should delay the game
     *         for a few ticks, to give the Player a break.
     */
    protected boolean shouldDelay(DungeonEvent event) {
        if (event.actor() == null)
            return false;
        return (event instanceof MoveSelectionEvent) || (event instanceof ItemSelectionEvent)
                || (event instanceof ItemSwappedEvent) || (event instanceof ItemMovedEvent);
    }

    @Override
    public boolean stopsTravel(DungeonEvent event) {
        return !(event instanceof PokemonTravelsEvent) && super.stopsTravel(event);
    }

}

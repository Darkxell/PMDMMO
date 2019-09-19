package com.darkxell.common.event;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.ai.states.AIStatePlayerControl;
import com.darkxell.common.dungeon.AutoDungeonExploration;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.DungeonOutcome.Outcome;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonSpawnedEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.dungeon.BossDefeatedEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.ExplorationStopEvent;
import com.darkxell.common.event.dungeon.MissionClearedEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.Logger;

/** Processes game logic events. */
public class CommonEventProcessor {
    public enum State {
        /** Playing animations. (should only be active client-side.) */
        ANIMATING,
        /** Waiting for a Player to decide on an action. */
        AWATING_INPUT,
        /** Playing delayed animations. (should only be active client-side.) */
        DELAYED,
        /** Processing pending events. */
        PROCESSING,
        /** Stopped. This state is set when the Dungeon is done exploring. For safety it will also prevent any further added events from being processed. */
        STOPPED
    }

    public final DungeonExploration dungeon;
    /** Pending events to process. */
    protected final Stack<Event> pending = new Stack<>();
    /** Lists the Players currently running. */
    private ArrayList<DungeonPokemon> runners = new ArrayList<>();
    /** While processing an event, setting this to false will stop processing the pending events. */
    private State state = State.PROCESSING;

    public CommonEventProcessor(DungeonExploration dungeon) {
        this.dungeon = dungeon;
    }

    /** Adds the input events to the pending stack, without processing them. */
    public void addToPending(ArrayList<Event> arrayList) {
        for (int i = arrayList.size() - 1; i >= 0; --i)
            this.addToPending(arrayList.get(i));
    }

    /** Adds the input event to the pending stack, without processing it. */
    public void addToPending(Event event) {
        if (event != null) {
            for (int i = this.pending.size() - 1; i >= 0; --i)
                if (this.pending.get(i).priority >= event.priority) {
                    this.pending.insertElementAt(event, i + 1);
                    return;
                }
            this.pending.insertElementAt(event, 0);
        }
    }

    /** Calls listeners to the input Event. */
    private void callPostListeners(Event event) {
        ArrayList<Event> resultingEvents = new ArrayList<>();
        for (DungeonPokemon p : this.dungeon.currentFloor().listPokemon()) {
            p.onPostEvent(this.dungeon.currentFloor(), event, p, resultingEvents);
            p.ability().onPostEvent(this.dungeon.currentFloor(), event, p, resultingEvents);
            for (AppliedStatusCondition condition : p.activeStatusConditions())
                condition.onPostEvent(this.dungeon.currentFloor(), event, p, resultingEvents);
        }

        for (Player player : this.dungeon.exploringPlayers())
            player.inventory().onPostEvent(this.dungeon.currentFloor(), event, null, resultingEvents);
        this.addToPending(resultingEvents);
    }

    /** Calls listeners to the input Event. */
    private void callPreListeners(Event event) {
        ArrayList<Event> resultingEvents = new ArrayList<>();
        for (DungeonPokemon p : this.dungeon.currentFloor().listPokemon()) {
            p.onPreEvent(this.dungeon.currentFloor(), event, p, resultingEvents);
            if (event.isConsumed()) break;
            p.ability().onPreEvent(this.dungeon.currentFloor(), event, p, resultingEvents);
            if (event.isConsumed()) break;
            for (AppliedStatusCondition condition : p.activeStatusConditions()) {
                condition.onPreEvent(this.dungeon.currentFloor(), event, p, resultingEvents);
                if (event.isConsumed()) break;
            }
        }

        for (Player player : this.dungeon.exploringPlayers())
            if (!event.isConsumed())
                player.inventory().onPreEvent(this.dungeon.currentFloor(), event, null, resultingEvents);
        this.addToPending(resultingEvents);
    }

    /** This Event is checked and ready to be processed. */
    protected void doProcess(Event event) {
        this.dungeon.eventOccured(event);
        this.addToPending(event.processServer());
        this.callPostListeners(event);
        if (event instanceof ExplorationStopEvent) this.setState(State.STOPPED);
        else if (event instanceof BossDefeatedEvent) this.processBossDefeatedEvent((BossDefeatedEvent) event);
        else if (event instanceof MissionClearedEvent && this.dungeon instanceof AutoDungeonExploration) {
            Event e = ((AutoDungeonExploration) this.dungeon).getNextEvent();
            if (e instanceof DungeonExitEvent)
                this.addToPending(((AutoDungeonExploration) this.dungeon).nextEvent());
        }
    }

    public boolean hasPendingEvents() {
        return this.pending.size() != 0;
    }

    public void onFloorStart(Floor floor) {}

    public void onTurnEnd() {
        this.addToPending(this.dungeon.endSubTurn());
        this.processPending();
    }

    /** Called just before processing an event.
     *
     * @return false if the event should not be processed.
     */
    protected boolean preProcess(Event event) {
        this.callPreListeners(event);

        if (!event.isConsumed() && event instanceof PokemonTravelEvent) {
            PokemonTravelEvent travel = (PokemonTravelEvent) event;
            if (travel.pokemon().isTeamLeader() && travel.running()) if (travel.destination().getPokemon() == null
                    || !travel.destination().getPokemon().isAlliedWith(travel.pokemon()))
                this.runners.add(travel.pokemon());

            // If leader is traveling onto ally's tile, automatically create ally movement event
            if (travel.destination().getPokemon() != null && travel.pokemon().isTeamLeader()
                    && travel.pokemon().isAlliedWith(travel.destination().getPokemon()))
                this.addToPending(new PokemonTravelEvent(this.dungeon.currentFloor(), BaseEventSource.PLAYER_ACTION,
                        travel.destination().getPokemon(), travel.direction().opposite()));
        }

        if (!event.isConsumed() && this.stopsTravel(event)) this.runners.clear();

        return !event.isConsumed() && event.isValid();
    }

    /** Method that handles a Boss defeat. This event has very different results depending on the Dungeon, so having a separate method for it seems necessary. */
    protected void processBossDefeatedEvent(BossDefeatedEvent event) {
        this.addToPending(new ExplorationStopEvent(this.dungeon.currentFloor(), event,
                new DungeonOutcome(Outcome.DUNGEON_CLEARED, this.dungeon.id)));
    }

    /** Processes the input event and adds the resulting events to the pending stack. */
    public void processEvent(Event event) {
        if (this.state() == State.STOPPED || event == null)
            return;
        this.setState(State.PROCESSING);
        if (!event.isConsumed() && this.preProcess(event)) this.doProcess(event);
        if (this.state() == State.PROCESSING) this.processPending();
    }

    /**
     * Adds all the input events to the pending stack and starts processing them. May not process all events in order if
     * some produce new Events.
     */
    public void processEvents(ArrayList<Event> events) {
        this.addToPending(events);
        this.processPending();
    }

    /** Processes the next pending event. */
    public void processPending() {
        if (this.state() == State.STOPPED) return;
        if (this.hasPendingEvents()) this.processEvent(this.pending.pop());
        else {
            DungeonPokemon actor = this.dungeon.nextActor();
            if (actor == null) {
                this.onTurnEnd();
                return;
            }

            AI ai = this.dungeon.currentFloor().aiManager.getAI(actor);

            if (ai == null)
                Logger.e("Null AI for " + actor + "!");
            if (ai != null && ai.currentState() instanceof AIStatePlayerControl && actor.canAct(this.dungeon.currentFloor())) {
                if (this.runners.contains(actor)) {
                    if (AIUtils.shouldStopRunning(actor)) {
                        this.runners.clear();
                        this.setState(State.AWATING_INPUT);
                    } else
                        this.processEvent(new PokemonTravelEvent(this.dungeon.currentFloor(),
                                BaseEventSource.PLAYER_ACTION, actor, true, actor.facing()));
                } else
                    this.setState(State.AWATING_INPUT);
            } else
                this.processEvent(this.dungeon.currentFloor().aiManager.takeAction(actor));
        }

        if (this.state() == State.AWATING_INPUT && this.dungeon instanceof AutoDungeonExploration) {
            Event event = ((AutoDungeonExploration) this.dungeon).nextEvent();
            if (event == null)
                event = new ExplorationStopEvent(this.dungeon.currentFloor(), BaseEventSource.PLAYER_ACTION, null);
            this.processEvent(event);
        }
    }

    protected void setState(State state) {
        this.state = state;
    }

    public State state() {
        return this.state;
    }

    public boolean stopsTravel(Event event) {
        /*
         * if (event instanceof ItemMovedEvent) { ItemMovedEvent e = (ItemMovedEvent) event; return !(e.source
         * instanceof Tile && (e.destination instanceof Inventory || e.destination instanceof Pokemon)); }
         */
        return !(event instanceof BellyChangedEvent || event instanceof TurnSkippedEvent
                || event instanceof PokemonRotateEvent || event instanceof PokemonTravelEvent
                || event instanceof PokemonSpawnedEvent/* || event instanceof MoneyCollectedEvent */);
    }

}

package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public abstract class DungeonEvent implements DungeonEventSource {

    /** Event that only displays a message. */
    public static class MessageEvent extends DungeonEvent {

        /** If not null, will only be displayed for this Player. */
        public final Player target;

        public MessageEvent(Floor floor, DungeonEventSource eventSource, Message message) {
            this(floor, eventSource, message, null);
        }

        public MessageEvent(Floor floor, DungeonEventSource eventSource, Message message, Player target) {
            super(floor, eventSource);
            this.target = target;
            this.messages.add(message);
        }

        @Override
        public String loggerMessage() {
            return "Showing message: " + this.messages.get(0);
        }
    }

    public static final byte PRIORITY_DEFAULT = 0, PRIORITY_AFTER_MOVE = 1, PRIORITY_ACTION_END = 2,
            PRIORITY_TURN_END = 3;

    /**
     * The Pokemon that performed the action triggering this Event. This action will consume its turn. May be null if no
     * performer or if this Event doesn't consume the actor's turn.
     */
    protected DungeonPokemon actor;
    /** True if messages generated by this Event should be displayed. */
    public boolean displayMessages = true;
    /** What created this Event. */
    public final DungeonEventSource eventSource;
    /** String to put flags for various behaviors. */
    private String flags = "";
    /** The Floor this Event occurs on. */
    public final Floor floor;
    /**
     * <code>true</code> if this Event has been cancelled. May be used if abilities have to change the effects or cancel
     * various events. If so, this Event won't be processed at all.
     */
    private boolean isConsumed;
    /**
     * True if this Event is a Player Action Event; and thus should be sent to other players in the Dungeon. If so, this
     * Event should implement Communicable, override equals() for unit tests and have a (Floor) Constructor.
     */
    private boolean isPAE;
    /** The messages that were generated. */
    protected ArrayList<Message> messages;
    /** The priority of this DungeonEvent. */
    protected byte priority;
    /** The events that resulted from this Event. */
    protected ArrayList<DungeonEvent> resultingEvents;

    public DungeonEvent(Floor floor, DungeonEventSource eventSource) {
        this(floor, eventSource, null);
    }

    public DungeonEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon actor) {
        this.floor = floor;
        this.eventSource = eventSource;
        this.setActor(actor);
        this.priority = PRIORITY_DEFAULT;
        this.messages = new ArrayList<>();
        this.resultingEvents = new ArrayList<>();
    }

    public DungeonPokemon actor() {
        return this.actor;
    }

    public void addFlag(String flag) {
        if (!this.hasFlag(flag))
            this.flags += (this.flags.equals("") ? "" : "|") + flag;
    }

    public void cloneFlags(DungeonEvent event) {
        for (String flag : event.flags.split(""))
            this.addFlag(flag);
    }

    /** Sets {@link DungeonEvent#isConsumed} to true. */
    public void consume() {
        this.isConsumed = true;
    }

    public String[] flags() {
        if (this.flags.equals(""))
            return new String[0];
        return this.flags.split(",");
    }

    /** @return The messages that were generated. */
    public Message[] getMessages() {
        return this.messages.toArray(new Message[0]);
    }

    /** @return The events that resulted from this Event. */
    public DungeonEvent[] getResultingEvents() {
        return this.resultingEvents.toArray(new DungeonEvent[0]);
    }

    public boolean hasFlag(String flag) {
        return this.flags.contains(flag);
    }

    /** @return True if this Event represents the turn Action of the actor Pokemon. */
    public boolean isAction() {
        return this.actor != null;
    }

    /** @return {@link DungeonEvent#isConsumed}. */
    public boolean isConsumed() {
        return this.isConsumed;
    }

    /** @return True if this Event is a PAE. */
    public boolean isPAE() {
        if (this.isPAE && !(this instanceof Communicable)) {
            Logger.e("PAE event doesn't implement Communicable!");
            return false;
        }
        return this.isPAE;
    }

    /**
     * @return True if this Event should occur. This needs to be checked when called in case other Events on the stack
     *         triggered actions that cancel this Event, such as fainting a Pokemon which is this Event's target.
     */
    public boolean isValid() {
        return true;
    }

    public abstract String loggerMessage();

    /**
     * Processes this Event server-side.
     *
     * @return The list of resulting Events.
     */
    public ArrayList<DungeonEvent> processServer() {
        return this.resultingEvents;
    }

    protected void setActor(DungeonPokemon actor) {
        this.actor = actor;
    }

    /** Defines this Event as a PAE. Make sure this Event implements Communicable and has a (Floor) Constructor. */
    public DungeonEvent setPAE() {
        this.isPAE = true;
        return this;
    }

    public DungeonEvent setPriority(byte priority) {
        this.priority = priority;
        return this;
    }

}

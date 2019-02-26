package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventListener;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class AppliedStatusCondition implements DungeonEventListener, EventSource {

    /** True if the Pokemon this Status condition affects has acted this turn while this Status condition was active.<br>
     * This is necessary due to the ticking of Status conditions happening at the end of turn: because Pokemon in teams act first, they would not suffer from conditions that prevent action for a single turn if this attribute wasn't used. */
    private boolean actedWhileApplied;
    /** This Status Condition's ID. */
    public final StatusCondition condition;
    /** This Status Condition's duration. */
    public final int duration;
    private String flags = "";
    /** The Pokemon this Status condition affects. */
    public final DungeonPokemon pokemon;
    public final Object source;
    /** The number of turns this Condition has been in effect. */
    public int tick;

    public AppliedStatusCondition(StatusCondition condition, DungeonPokemon pokemon, Object source, int duration) {
        this.condition = condition;
        this.pokemon = pokemon;
        this.source = source;
        this.duration = duration;
        this.tick = 0;
    }

    /** @return {@link AppliedStatusCondition#actedWhileApplied}. */
    public boolean actedWhileApplied() {
        return this.actedWhileApplied;
    }

    public void addFlag(String flag) {
        if (!this.hasFlag(flag)) {
            if (!this.flags.equals("")) this.flags += "|";
            this.flags += flag;
        }
    }

    public Message endMessage() {
        String id = "status.end." + this.condition.id;
        if (!Localization.containsKey(id)) return null;
        return new Message(id).addReplacement("<pokemon>", this.pokemon.getNickname());
    }

    public void finish(Floor floor, StatusConditionEndReason reason, EventSource finishSource,
            ArrayList<Event> events) {
        events.add(new StatusConditionEndedEvent(floor, finishSource, this, reason));
    }

    public int getTurns() {
        return this.tick;
    }

    public boolean hasFlag(String flag) {
        return this.flags.contains(flag);
    }

    public boolean isOver() {
        if (this.duration == -1) return false;
        return this.tick >= this.duration;
    }

    public String[] listFlags() {
        if (this.flags.equals("")) return new String[0];
        return this.flags.split("\\|");
    }

    public void onConditionEnd(StatusConditionEndedEvent event, ArrayList<Event> events) {
        this.condition.onEnd(event, events);
    }

    public void onConditionStart(Floor floor, ArrayList<Event> events) {
        this.condition.onStart(floor, this, events);
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        this.condition.onPostEvent(floor, event, concerned, resultingEvents);
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        this.condition.onPreEvent(floor, event, concerned, resultingEvents);
    }

    public void setActedWhileApplied() {
        this.actedWhileApplied = true;
    }

    public Message startMessage() {
        String id = "status.start." + this.condition.id;
        if (!Localization.containsKey(id)) return null;
        return new Message(id).addReplacement("<pokemon>", this.pokemon.getNickname());
    }

    public void tick(Floor floor, ArrayList<Event> events) {
        if (!this.isOver())
            this.condition.tick(floor, this, events);
        ++this.tick;
        this.actedWhileApplied = false;
        if (this.isOver()) this.finish(floor, StatusConditionEndReason.FINISHED, this, events);
    }

}

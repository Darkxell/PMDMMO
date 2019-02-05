package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class StatusConditionCreatedEvent extends DungeonEvent {
    public final AppliedStatusCondition condition;
    private boolean succeeded = false;

    public StatusConditionCreatedEvent(Floor floor, AppliedStatusCondition condition) {
        super(floor);
        this.condition = condition;
    }

    @Override
    public boolean isValid() {
        return !this.condition.pokemon.isFainted();
    }

    @Override
    public String loggerMessage() {
        return "Created " + this.condition.condition.name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        Pair<Boolean, Message> affects = this.condition.condition.affects(this.floor, this.condition,
                this.condition.pokemon);
        this.succeeded = affects.first;
        if (affects.second != null)
            this.messages.add(affects.second);
        if (this.succeeded) {
            Message m = this.condition.startMessage();
            if (m != null)
                this.messages.add(m);
            this.condition.pokemon.inflictStatusCondition(this.condition);
            this.condition.onConditionStart(floor, this.resultingEvents);
        }
        return super.processServer();
    }

    /** @return <code>true</code> if the Status Condition was successfully applied. */
    public boolean succeeded() {
        return this.succeeded;
    }

}

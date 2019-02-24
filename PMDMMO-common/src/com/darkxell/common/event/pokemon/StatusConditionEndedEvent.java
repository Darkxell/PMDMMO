package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.language.Message;

public class StatusConditionEndedEvent extends DungeonEvent {
    public enum StatusConditionEndReason {
        /** If it was stopped by another effect. */
        BROKEN,
        /** If the status condition cannot continue (for example, if a target doesn't exist anymore). */
        CANT_CONTINUE,
        /** If it finished naturally (it reached its duration). */
        FINISHED,
        /** If it was healed. */
        HEALED,
        /** If it was prevented. */
        PREVENTED
    }

    public final AppliedStatusCondition condition;
    public final StatusConditionEndReason reason;

    public StatusConditionEndedEvent(Floor floor, AppliedStatusCondition condition, StatusConditionEndReason reason) {
        super(floor, eventSource);
        this.condition = condition;
        this.reason = reason;
    }

    @Override
    public String loggerMessage() {
        return this.condition.condition.name() + " finished for " + this.condition.pokemon.getNickname();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        if (this.condition.pokemon.removeStatusCondition(this.condition)) {
            this.condition.onConditionEnd(this.floor, this.reason, this.resultingEvents);
            Message m = this.condition.endMessage();
            if (m != null)
                this.messages.add(m);
        }
        return super.processServer();
    }

}

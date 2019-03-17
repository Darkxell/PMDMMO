package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.states.AIStateChargedAttack;
import com.darkxell.common.ai.states.AIStateTurnSkipper;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.status.AppliedStatusCondition;

public class ChargedMoveStatusCondition extends ChangeAIStatusCondition {

    public final int moveID;

    public ChargedMoveStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int moveID) {
        super(id, isAilment, durationMin, durationMax);
        this.moveID = moveID;
    }

    @Override
    public void onEnd(StatusConditionEndedEvent event, ArrayList<Event> events) {
        super.onEnd(event, events);
        event.floor.aiManager.getAI(event.condition.pokemon).setSuperState(null);
    }

    @Override
    public void onStart(Floor floor, AppliedStatusCondition instance, ArrayList<Event> events) {
        super.onStart(floor, instance, events);
        AI ai = floor.aiManager.getAI(instance.pokemon);
        if (instance.duration == 1)
            ai.setSuperState(new AIStateChargedAttack(ai, this.moveID));
        else
            ai.setSuperState(new AIStateTurnSkipper(ai));
    }

    @Override
    public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<Event> events) {
        super.tick(floor, instance, events);
        if (instance.tick == instance.duration - 2) {
            AI ai = floor.aiManager.getAI(instance.pokemon);
            ai.setSuperState(new AIStateChargedAttack(ai, this.moveID));
        }
    }

}

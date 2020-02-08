package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.LearnedMove;

public class ConditionalEffect extends MoveEffect {

    public static interface EffectCondition {

        boolean isMet(MoveUseEvent moveEvent, ArrayList<Event> events);

    }

    public final EffectCondition condition;
    public final int moveIfTrue, moveIfFalse;

    public ConditionalEffect(int moveIfTrue, int moveIfFalse, EffectCondition condition) {
        this.moveIfTrue = moveIfTrue;
        this.moveIfFalse = moveIfFalse;
        this.condition = condition;
    }

    @Override
    public boolean mainUse(MoveUseEvent moveEvent, ArrayList<Event> events) {
        int moveToUse = this.moveIfFalse;
        if (this.condition.isMet(moveEvent, events)) moveToUse = this.moveIfTrue;
        events.add(new MoveSelectionEvent(moveEvent.floor, moveEvent, new LearnedMove(moveToUse),
                moveEvent.usedMove.user, moveEvent.direction, false));
        return false;
    }

}

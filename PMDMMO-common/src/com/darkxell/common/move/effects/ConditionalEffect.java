package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class ConditionalEffect extends MoveEffect {

    public static interface EffectCondition {

        boolean isMet(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
                ArrayList<DungeonEvent> events);

    }

    public final EffectCondition condition;
    public final int moveIfTrue, moveIfFalse;

    public ConditionalEffect(int id, int moveIfTrue, int moveIfFalse, EffectCondition condition) {
        super(id);
        this.moveIfTrue = moveIfTrue;
        this.moveIfFalse = moveIfFalse;
        this.condition = condition;
    }

    @Override
    public boolean mainUse(MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        int moveToUse = this.moveIfFalse;
        if (this.condition.isMet(moveEvent, target, flags, floor, events))
            moveToUse = this.moveIfTrue;
        events.add(new MoveSelectionEvent(floor, eventSource, new LearnedMove(moveToUse), moveEvent.user, moveEvent.direction, false));
        return false;
    }

}

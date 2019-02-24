package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;

public class CannotKOEffect extends MoveEffect {

    public CannotKOEffect(int id) {
        super(id);
    }

    @Override
    public double applyDamageModifications(double damage, boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        if (damage >= moveEvent.target.getHp()) return moveEvent.target.getHp() - 1;
        return super.applyDamageModifications(damage, isUser, moveEvent, events);
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;

public class HPMultiplierEffect extends MoveEffect {

    public HPMultiplierEffect(int id) {
        super(id);
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        double hp = moveEvent.usedMove.user.getHpPercentage();
        if (hp < 25)
            return 8;
        if (hp < 50)
            return 4;
        if (hp < 75)
            return 2;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

}

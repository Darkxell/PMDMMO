package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class HPMultiplierEffect extends MoveEffect {

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<Event> events) {
        double hp = moveEvent.usedMove.user.getHpPercentage();
        if (hp < .25)
            return 8;
        if (hp < .5)
            return 4;
        if (hp < .75)
            return 2;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

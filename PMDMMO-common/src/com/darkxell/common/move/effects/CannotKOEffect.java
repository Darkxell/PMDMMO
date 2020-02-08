package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class CannotKOEffect extends MoveEffect {

    @Override
    public double applyDamageModifications(double damage, boolean isUser, MoveUseEvent moveEvent,
            ArrayList<Event> events) {
        if (damage >= moveEvent.target.getHp())
            return moveEvent.target.getHp() - 1;
        return super.applyDamageModifications(damage, isUser, moveEvent, events);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

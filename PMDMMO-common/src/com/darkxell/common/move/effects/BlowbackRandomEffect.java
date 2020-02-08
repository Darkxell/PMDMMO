package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.Direction;

public class BlowbackRandomEffect extends BlowbackEffect {

    @Override
    protected Direction direction(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects) {
        return Direction.randomDirection(moveEvent.floor.random);
    }

}

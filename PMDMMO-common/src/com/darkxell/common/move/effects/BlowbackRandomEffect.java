package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.util.Direction;

public class BlowbackRandomEffect extends BlowbackEffect {

    @Override
    protected Direction direction(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects) {
        return Direction.randomDirection(context.floor.random);
    }

}

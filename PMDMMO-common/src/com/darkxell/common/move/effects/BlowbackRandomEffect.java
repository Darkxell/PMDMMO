package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.util.Direction;

public class BlowbackRandomEffect extends BlowbackEffect {

    public BlowbackRandomEffect(int id) {
        super(id);
    }

    @Override
    protected Direction direction(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        return Direction.randomDirection(moveEvent.floor.random);
    }

}

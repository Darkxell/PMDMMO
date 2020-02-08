package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.Direction;

public class BlowbackEffect extends MoveEffect {

    protected Direction direction(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        return moveEvent.usedMove.direction;
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            effects.add(new BlowbackPokemonEvent(moveEvent.floor, moveEvent, moveEvent.target,
                    this.direction(moveEvent, calculator, missed, effects)));
        }
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.util.Direction;

public class BlowbackEffect extends MoveEffect {

    protected Direction direction(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        return context.event.usedMove.direction;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            effects.add(new BlowbackPokemonEvent(context.floor, context.event, context.target,
                    this.direction(context, calculator, missed, effects)));
        }
    }

}

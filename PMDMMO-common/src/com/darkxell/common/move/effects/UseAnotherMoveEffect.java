package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.LearnedMove;

public abstract class UseAnotherMoveEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!createAdditionals) {
            Move chosen = this.moveToUse(context, calculator, missed, effects);
            if (chosen != null)
                effects.add(new MoveSelectionEvent(context.floor, context.event, new LearnedMove(chosen.getID()),
                        context.user));
        }
    }

    protected abstract Move moveToUse(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects);

}

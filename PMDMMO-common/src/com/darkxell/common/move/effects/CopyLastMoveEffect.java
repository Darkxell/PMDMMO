package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonUtils;
import com.darkxell.common.event.Event;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.behavior.MoveBehaviors;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class CopyLastMoveEffect extends UseAnotherMoveEffect {

    @Override
    protected Move moveToUse(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        DungeonPokemon target = context.target;
        LearnedMove m = DungeonUtils.findLastMove(context.floor, target);
        if (m == null)
            return null;
        Move move = m.move();
        int behavior = move.behavior().id;
        if (behavior == context.moveBehavior.id)
            return null;
        if (behavior == MoveBehaviors.Use_random_move_on_floor.id)
            return null;
        if (behavior == MoveBehaviors.Inflict_encore.id)
            return null;
        if (behavior == MoveBehaviors.Inflict_mirrormove.id)
            return null;
        return move;
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.RandomUtil;

public class RandomMoveEffect extends UseAnotherMoveEffect {

    @Override
    protected Move moveToUse(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        ArrayList<Move> moves = new ArrayList<>();
        for (DungeonPokemon p : context.floor.listPokemon())
            for (int m = 0; m < p.moveCount(); ++m) {
                LearnedMove move = p.move(m);
                if (move.moveId() != context.move.getID())
                    moves.add(move.move());
            }

        return RandomUtil.random(moves, context.floor.random);
    }

}

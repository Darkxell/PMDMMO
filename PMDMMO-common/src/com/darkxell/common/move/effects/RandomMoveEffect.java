package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.RandomUtil;

public class RandomMoveEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!createAdditionals) {
            ArrayList<Move> moves = new ArrayList<>();
            for (DungeonPokemon p : moveEvent.floor.listPokemon())
                for (int m = 0; m < p.moveCount(); ++m) {
                    LearnedMove move = p.move(m);
                    if (move.moveId() != moveEvent.usedMove.move.moveId())
                        moves.add(move.move());
                }

            Move chosen = RandomUtil.random(moves, moveEvent.floor.random);
            effects.add(new MoveSelectionEvent(moveEvent.floor, moveEvent, new LearnedMove(chosen.id),
                    moveEvent.usedMove.user));
        }
    }

}

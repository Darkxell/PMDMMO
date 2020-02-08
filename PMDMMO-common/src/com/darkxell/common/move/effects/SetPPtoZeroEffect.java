package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonUtils;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.pokemon.LearnedMove;

public class SetPPtoZeroEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            LearnedMove move = DungeonUtils.findLastMove(moveEvent.floor, moveEvent.target);
            if (move != null && move.pp() > 0)
                effects.add(new PPChangedEvent(moveEvent.floor, moveEvent, moveEvent.target, -move.pp(),
                        moveEvent.target.moveIndex(move)));
        }
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.DungeonUtils;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.pokemon.LearnedMove;

public class SetPPtoZeroEffect extends MoveEffect {

    public SetPPtoZeroEffect(int id) {
        super(id);
    }

    @Override
    protected void mainEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.mainEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            LearnedMove move = DungeonUtils.findLastMove(moveEvent.floor, moveEvent.target);
            if (move != null && move.pp() > 0)
                effects.createEffect(new PPChangedEvent(moveEvent.floor, moveEvent, moveEvent.target, -move.pp(), moveEvent.target.moveIndex(move)),
                        false);
        }
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.Direction;

public class BlowbackEffect extends MoveEffect {

    public BlowbackEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            effects.createEffect(new BlowbackPokemonEvent(moveEvent.floor, moveEvent, moveEvent.target,
                    this.direction(moveEvent, calculator, missed, effects)), moveEvent, missed, false);
        }
    }

    protected Direction direction(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        return moveEvent.usedMove.direction;
    }

}

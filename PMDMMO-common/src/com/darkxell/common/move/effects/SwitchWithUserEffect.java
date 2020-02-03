package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.SwitchedPokemonEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class SwitchWithUserEffect extends MoveEffect {

    public SwitchWithUserEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed) effects.createEffect(new SwitchedPokemonEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user, moveEvent.target), false);
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class DealDamageToUserIfMissEffect extends MoveEffect {

    public final double damageDealt;

    public DealDamageToUserIfMissEffect(int id, double percentDamageDealt) {
        super(id);
        this.damageDealt = percentDamageDealt;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (missed) {
            effects.createEffect(new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user,
                    moveEvent.usedMove, DamageType.RECOIL, this.getDamage(calculator)), true);
        }
    }

    private int getDamage(MoveEffectCalculator calculator) {
        return (int) (calculator.compute(new ArrayList<>()) * this.damageDealt);
    }

}

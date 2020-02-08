package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class DealDamageToUserIfMissEffect extends MoveEffect {

    public final double damageDealt;

    public DealDamageToUserIfMissEffect(double percentDamageDealt) {
        this.damageDealt = percentDamageDealt;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (missed && !createAdditionals) {
            effects.add(new DamageDealtEvent(context.floor, context.event, context.user, context.event.usedMove,
                    DamageType.RECOIL, this.getDamage(calculator)));
        }
    }

    private int getDamage(MoveEffectCalculator calculator) {
        return (int) (calculator.compute(new ArrayList<>()) * this.damageDealt);
    }

}

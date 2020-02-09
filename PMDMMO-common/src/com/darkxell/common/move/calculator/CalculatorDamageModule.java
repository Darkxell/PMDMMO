package com.darkxell.common.move.calculator;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;

public interface CalculatorDamageModule {

    public static class DefaultDamageModule implements CalculatorDamageModule {
    }

    /**
     * Computes the damage dealt by the move.
     * 
     * @param  context    - The Move use context.
     * @param  calculator - The calculator object.
     * @param  events     - Events while the calculator is resolving.
     * @return            The damage dealt.
     */
    public default int compute(MoveContext context, MoveEffectCalculator calculator, ArrayList<Event> events) {
        int attack = calculator.attackStat(events);
        int defense = calculator.defenseStat(events);
        int level = context.user.level();
        int power = calculator.movePower();
        double wildNerfer = context.user.isEnemy() ? 1 : 0.75;

        double damage = ((attack + power) * 0.6 - defense / 2
                + 50 * Math.log(((attack - defense) / 8 + level + 50) * 10) - 311) * wildNerfer;
        if (damage < 1)
            damage = 1;
        if (damage > 999)
            damage = 999;

        double multiplier = calculator.damageMultiplier(calculator.criticalLands(events), events);
        damage *= multiplier;

        // Damage randomness
        damage *= (9 - context.floor.random.nextDouble() * 2) / 8;

        damage = calculator.modificator.applyDamageModifications(damage, context, events);

        return (int) Math.round(damage);
    }

}

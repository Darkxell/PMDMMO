package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.stats.StatsResetEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;

public class ResetStatsEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && context.target != null && context.move.isDealsDamage() == createAdditionals) {
            effects.add(new StatsResetEvent(context.floor, context.event, context.target));
        }
    }

}

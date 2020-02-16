package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.util.language.Message;

public class ResetStatEffect extends MoveEffect {

    public final Stat stat;

    public ResetStatEffect(Stat stat) {
        this.stat = stat;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && context.target != null && createAdditionals == context.move.isDealsDamage()) {
            int stage = context.target.stats.getStage(this.stat);
            if (stage > 10)
                effects.add(new StatChangedEvent(context.floor, context.event, context.target, this.stat, 10 - stage));
        }
    }

    @Override
    public Message description() {
        return new Message("move.info.stat_reset").addReplacement("<stat>", this.stat.getName());
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.util.language.Message;

public class CopyStatChangesEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && !createAdditionals) {
            DungeonStats ts = context.target.stats, us = context.user.stats;
            boolean first = true;
            for (Stat stat : Stat.values()) {
                int diff = ts.getStage(stat) - us.getStage(stat);
                if (diff != 0) {
                    if (first) {
                        first = false;
                        effects.add(new MessageEvent(context.floor, context.event,
                                new Message("stats.copied").addReplacement("<pokemon>", context.target.getNickname())));
                    }
                    effects.add(new StatChangedEvent(context.floor, context.event, context.user, stat, diff));
                }
            }
            if (first)
                effects.add(new MessageEvent(context.floor, context.event,
                        new Message("stats.copied.none").addReplacement("<pokemon>", context.target.getNickname())));
        }
    }

}

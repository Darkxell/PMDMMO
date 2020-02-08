package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.util.language.Message;

public class CopyStatChangesEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {
        if (!missed && !createAdditionals) {
            DungeonStats ts = moveEvent.target.stats, us = moveEvent.usedMove.user.stats;
            boolean first = true;
            for (Stat stat : Stat.values()) {
                int diff = ts.getStage(stat) - us.getStage(stat);
                if (diff != 0) {
                    if (first) {
                        first = false;
                        effects.add(new MessageEvent(moveEvent.floor, moveEvent, new Message("stats.copied")
                                .addReplacement("<pokemon>", moveEvent.target.getNickname())));
                    }
                    effects.add(new StatChangedEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user, stat, diff));
                }
            }
            if (first)
                effects.add(new MessageEvent(moveEvent.floor, moveEvent,
                        new Message("stats.copied.none").addReplacement("<pokemon>", moveEvent.target.getNickname())));
        }
    }

}

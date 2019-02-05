package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.util.language.Message;

public class CopyStatChangesEffect extends MoveEffect {

    public CopyStatChangesEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

        if (!missed) {
            DungeonStats ts = target.stats, us = usedMove.user.stats;
            boolean first = true;
            for (Stat stat : Stat.values()) {
                int diff = ts.getStage(stat) - us.getStage(stat);
                if (diff != 0) {
                    if (first) {
                        first = false;
                        effects.events.add(new MessageEvent(floor,
                                new Message("stats.copied").addReplacement("<pokemon>", target.getNickname())));
                    }
                    effects.createEffect(new StatChangedEvent(floor, usedMove.user, stat, diff, usedMove), usedMove,
                            target, floor, missed, false, usedMove.user);
                }
            }
            if (first)
                effects.events.add(new MessageEvent(floor,
                        new Message("stats.copied.none").addReplacement("<pokemon>", target.getNickname())));
        }
    }

}

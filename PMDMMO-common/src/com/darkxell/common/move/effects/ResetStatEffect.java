package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class ResetStatEffect extends MoveEffect {

    public final Stat stat;

    public ResetStatEffect(int id, Stat stat) {
        super(id);
        this.stat = stat;
    }

    @Override
    public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);
        if (!missed) {
            int stage = target.stats.getStage(this.stat);
            if (stage > 10)
                effects.createEffect(new StatChangedEvent(floor, eventSource, target, this.stat, 10 - stage, usedMove), usedMove,
                        target, floor, missed, usedMove.move.move().dealsDamage, target);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.stat_reset").addReplacement("<stat>", this.stat.getName());
    }

}

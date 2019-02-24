package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.util.language.Message;

public class ResetStatEffect extends MoveEffect {

    public final Stat stat;

    public ResetStatEffect(int id, Stat stat) {
        super(id);
        this.stat = stat;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);
        if (!missed) {
            int stage = moveEvent.target.stats.getStage(this.stat);
            if (stage > 10) effects.createEffect(new StatChangedEvent(moveEvent.floor, moveEvent, moveEvent.target, this.stat, 10 - stage, moveEvent),
                    moveEvent, missed, moveEvent.usedMove.move.move().dealsDamage, moveEvent.target);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.stat_reset").addReplacement("<stat>", this.stat.getName());
    }

}

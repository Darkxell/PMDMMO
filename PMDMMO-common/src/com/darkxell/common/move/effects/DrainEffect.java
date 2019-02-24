package com.darkxell.common.move.effects;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.language.Message;

public class DrainEffect extends MoveEffect {

    public final int percent;

    public DrainEffect(int id, int percent) {
        super(id);
        this.percent = percent;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);
        if (!missed) {
            DamageDealtEvent damage = null;
            for (DungeonEvent e : effects.events)
                if (e instanceof DamageDealtEvent) {
                    damage = (DamageDealtEvent) e;
                    break;
                }
            if (damage != null)
                effects.createEffect(new HealthRestoredEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user,
                        damage.damage * this.percent / 100), moveEvent, missed, true);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.drain").addReplacement("<percent>", String.valueOf(this.percent));
    }

}

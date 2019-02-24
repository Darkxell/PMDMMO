package com.darkxell.common.move.effects;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.language.Message;

public class RecoilEffect extends MoveEffect {

    public final double percentage;

    public RecoilEffect(int id, double percentage) {
        super(id);
        this.percentage = percentage;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);
        if (!missed) {
            int damage = -1;
            for (DungeonEvent e : effects.events)
                if (e instanceof DamageDealtEvent) {
                    DamageDealtEvent d = (DamageDealtEvent) e;
                    if (d.target == moveEvent.target && d.source == moveEvent)
                        damage = d.damage;
                }
            damage *= this.percentage / 100;
            effects.createEffect(new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user,
                    moveEvent.usedMove, DamageType.RECOIL, damage), moveEvent, missed, true);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.recoil").addReplacement("<percent>", String.valueOf(this.percentage));
    }

}

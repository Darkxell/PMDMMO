package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.language.Message;

public class HPRecoilEffect extends MoveEffect {

    public final double percent;

    public HPRecoilEffect(int id, double percent) {
        super(id);
        this.percent = percent;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);
        if (!missed) {
            int damage = moveEvent.user.getMaxHP();
            damage *= this.percent / 100;
            effects.createEffect(new DamageDealtEvent(floor, eventSource, moveEvent.user, moveEvent, DamageType.RECOIL, damage),
                    moveEvent, missed, true, moveEvent.user);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.recoil_hp").addReplacement("<percent>", String.valueOf(this.percent));
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.language.Message;

public class HPRecoilEffect extends MoveEffect {

    public final double percent;

    public HPRecoilEffect(double percent) {
        this.percent = percent;
    }

    @Override
    public Message description() {
        return new Message("move.info.recoil_hp").addReplacement("<percent>", String.valueOf(this.percent));
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {
        if (!missed && createAdditionals) {
            int damage = moveEvent.usedMove.user.getMaxHP();
            damage *= this.percent / 100;
            effects.add(new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user, moveEvent.usedMove,
                    DamageType.RECOIL, damage));
        }
    }

}

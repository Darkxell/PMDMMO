package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.language.Message;

public class UserPercentDamageEffect extends MoveEffect {

    public final double percent;

    public UserPercentDamageEffect(int id, double percent) {
        super(id);
        this.percent = percent;
    }

    @Override
    protected void mainEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.mainEffects(moveEvent, calculator, missed, effects);

        effects.createEffect(
                new DamageDealtEvent(floor, eventSource, moveEvent.user, moveEvent,
                        DamageType.MOVE, (int) (moveEvent.user.getMaxHP() * this.percent)),
                moveEvent, missed, false, moveEvent.user);
    }

    @Override
    public Message descriptionBase(Move move) {
        String id = "move.info.damage_user_percent";
        return new Message(id).addReplacement("<percent>", String.valueOf(this.percent * 100));
    }

}

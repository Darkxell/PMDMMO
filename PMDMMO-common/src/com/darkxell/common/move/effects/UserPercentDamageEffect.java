package com.darkxell.common.move.effects;

import com.darkxell.common.event.Event;
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
    protected void mainEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.mainEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            Event event = new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user, moveEvent.usedMove,
                    DamageType.MOVE, (int) (moveEvent.usedMove.user.getMaxHP() * this.percent));
            effects.createEffect(event, moveEvent, missed, false);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        String id = "move.info.damage_user_percent";
        return new Message(id).addReplacement("<percent>", String.valueOf(this.percent * 100));
    }

}

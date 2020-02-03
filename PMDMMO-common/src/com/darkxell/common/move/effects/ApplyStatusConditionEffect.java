package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public class ApplyStatusConditionEffect extends MoveEffect {

    public final int probability;
    public final StatusCondition status;

    public ApplyStatusConditionEffect(int id, StatusCondition status, int probability) {
        super(id);
        this.status = status;
        this.probability = probability;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (this.shouldApply(moveEvent, calculator, missed, effects))
            effects.createEffect(
                    new StatusConditionCreatedEvent(moveEvent.floor, moveEvent,
                            this.status.create(moveEvent.floor, moveEvent.target, moveEvent.usedMove.user)),
                    moveEvent.usedMove.move.move().dealsDamage);
    }

    @Override
    public Message descriptionBase(Move move) {
        String id = "move.info.inflict_status_cond";
        if (this.probability < 100)
            id = "move.info.inflict_status_cond_maybe";
        return new Message(id).addReplacement("<status>", this.status.name()).addReplacement("<percent>",
                String.valueOf(this.probability));
    }

    protected boolean shouldApply(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        return !missed && moveEvent.floor.random.nextDouble() * 100 < this.probability;
    }

}

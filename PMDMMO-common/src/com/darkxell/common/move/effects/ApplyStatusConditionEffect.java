package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public class ApplyStatusConditionEffect extends MoveEffect {

    public final int probability;
    public final StatusCondition status;

    public ApplyStatusConditionEffect(StatusCondition status, int probability) {
        this.status = status;
        this.probability = probability;
    }

    @Override
    public Message description() {
        String id = "move.info.inflict_status_cond";
        if (this.probability < 100)
            id = "move.info.inflict_status_cond_maybe";
        return new Message(id).addReplacement("<status>", this.status.name()).addReplacement("<percent>",
                String.valueOf(this.probability));
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (this.shouldApply(context, calculator, missed, effects)
                && createAdditionals == context.move.dealsDamage) {
            effects.add(new StatusConditionCreatedEvent(context.floor, context.event,
                    this.status.create(context.floor, context.target, context.user)));
        }
    }

    protected boolean shouldApply(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        return !missed && context.floor.random.nextDouble() * 100 < this.probability;
    }

}

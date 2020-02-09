package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.FloorStatusCreatedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.status.FloorStatus;
import com.darkxell.common.util.language.Message;

public class CreateFloorStatusEffect extends MoveEffect {

    public final FloorStatus status;

    public CreateFloorStatusEffect(FloorStatus status) {
        this.status = status;
    }

    @Override
    public Message description() {
        return new Message("move.info.floor_status").addReplacement("<status>", this.status.name());
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && createAdditionals == context.move.dealsDamage) {
            effects.add(new FloorStatusCreatedEvent(context.floor, context.event,
                    this.status.create(context.user, context.floor.random)));
        }
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.FloorStatusCreatedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
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
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && createAdditionals == moveEvent.usedMove.move.move().dealsDamage) {
            effects.add(new FloorStatusCreatedEvent(moveEvent.floor, moveEvent,
                    this.status.create(moveEvent.usedMove.user, moveEvent.floor.random)));
        }
    }

}

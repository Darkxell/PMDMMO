package com.darkxell.common.move.effects;

import com.darkxell.common.event.dungeon.FloorStatusCreatedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.status.FloorStatus;
import com.darkxell.common.util.language.Message;

public class CreateFloorStatusEffect extends MoveEffect {

    public final FloorStatus status;

    public CreateFloorStatusEffect(int id, FloorStatus status) {
        super(id);
        this.status = status;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed) effects.createEffect(
                new FloorStatusCreatedEvent(moveEvent.floor, moveEvent,
                        this.status.create(moveEvent.usedMove.user, moveEvent.floor.random)),
                moveEvent, missed, moveEvent.usedMove.move.move().dealsDamage);
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.floor_status").addReplacement("<status>", this.status.name());
    }

}

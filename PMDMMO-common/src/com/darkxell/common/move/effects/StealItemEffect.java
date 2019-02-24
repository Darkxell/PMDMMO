package com.darkxell.common.move.effects;

import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class StealItemEffect extends MoveEffect {

    public StealItemEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed && target.hasItem() && !moveEvent.user.hasItem())
            effects.createEffect(
                    new ItemMovedEvent(floor, eventSource, ItemAction.STEAL, moveEvent.user, target, 0, moveEvent.user, 0, false),
                    moveEvent, missed, true, target);
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class DropItemEffect extends MoveEffect {

    public DropItemEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed && moveEvent.target.hasItem())
            effects.createEffect(new ItemMovedEvent(moveEvent.floor, moveEvent, ItemAction.AUTO, null, moveEvent.target,
                    0, moveEvent.target.tile(), 0), moveEvent, missed, true);
    }

}

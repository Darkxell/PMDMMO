package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class DropItemEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && moveEvent.target.hasItem() && createAdditionals)
            effects.add(new ItemMovedEvent(moveEvent.floor, moveEvent, ItemAction.AUTO, null, moveEvent.target, 0,
                    moveEvent.target.tile(), 0));
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.item.ItemAction;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;

public class DropItemEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && context.target != null && context.target.hasItem() && createAdditionals)
            effects.add(new ItemMovedEvent(context.floor, context.event, ItemAction.AUTO, null, context.target, 0,
                    context.target.tile(), 0));
    }

}

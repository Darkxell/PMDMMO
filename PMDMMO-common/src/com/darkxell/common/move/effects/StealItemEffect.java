package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;

public class StealItemEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && context.target.hasItem() && !context.user.hasItem() && createAdditionals)
            effects.add(new ItemMovedEvent(context.floor, context.event, ItemAction.STEAL, context.user, context.target,
                    0, context.user, 0, false));
    }

}

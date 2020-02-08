package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemCreatedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;

public class DropsMoneyOnKillEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && context.target != null && createAdditionals) {
            ItemStack item = new ItemStack(Item.POKEDOLLARS, context.floor.getMoneyQuantity());
            ItemCreatedEvent event = new ItemCreatedEvent(context.floor, context.event, item, context.target.tile()) {
                @Override
                public boolean isValid() {
                    if (!context.target.isFainted())
                        return false;
                    return super.isValid();
                }
            };
            effects.add(event);
        }
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.item.ItemCreatedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class DropsMoneyOnKillEffect extends MoveEffect {

    public DropsMoneyOnKillEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed && target != null) {
            ItemStack item = new ItemStack(Item.POKEDOLLARS, floor.getMoneyQuantity());
            ItemCreatedEvent event = new ItemCreatedEvent(floor, eventSource, item, target.tile()) {
                @Override
                public boolean isValid() {
                    if (!target.isFainted())
                        return false;
                    return super.isValid();
                }
            };
            effects.createEffect(event, moveEvent, missed, true, null);
        }
    }

}

package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;

/** An Item that restores belly when eaten, and inflicts negative status effects. */
public class HealFoodItemEffect extends FoodItemEffect {

    /** The amount of health this Item restores when eaten. */
    public final int hp;
    /** The amount of health this Item adds to the maximum health when eaten with full health. */
    public final int hpFull;

    public HealFoodItemEffect(int id, int food, int bellyIfFull, int belly, int hp, int hpFull) {
        super(id, food, bellyIfFull, belly);
        this.hp = hp;
        this.hpFull = hpFull;
    }

    @Override
    public boolean isUsedOnTeamMember() {
        return true;
    }

    @Override
    public void use(ItemUseEvent itemEvent, ArrayList<DungeonEvent> events) {
        super.use(itemEvent, events);
        events.add(new HealthRestoredEvent(itemEvent, eventSource, target, this.hp));
    }

}

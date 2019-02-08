package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class CureStatusFoodItemEffect extends FoodItemEffect {

    public final StatusCondition[] conditions;

    public CureStatusFoodItemEffect(int id, int food, int belly, int bellyIfFull, StatusCondition... conditions) {
        super(id, food, belly, bellyIfFull);
        this.conditions = conditions;
    }

    @Override
    public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target,
            ArrayList<DungeonEvent> events) {
        super.use(floor, item, pokemon, target, events);
        for (StatusCondition c : this.conditions)
            if (target.hasStatusCondition(c))
                target.getStatusCondition(c).finish(floor, StatusConditionEndReason.HEALED, events);
    }

}

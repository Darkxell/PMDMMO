package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class InflictStatusFoodItemEffect extends FoodItemEffect {

    public final StatusCondition status;

    public InflictStatusFoodItemEffect(int id, int food, int belly, int bellyIfFull, StatusCondition status) {
        super(id, food, belly, bellyIfFull);
        this.status = status;
    }

    @Override
    public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target,
            ArrayList<DungeonEvent> events) {
        super.use(floor, item, pokemon, target, events);
        events.add(new StatusConditionCreatedEvent(floor, this.status.create(floor, target, item, floor.random)));
    }

}

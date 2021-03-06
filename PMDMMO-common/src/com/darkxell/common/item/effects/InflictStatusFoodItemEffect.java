package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.status.StatusCondition;

public class InflictStatusFoodItemEffect extends FoodItemEffect {

    public final StatusCondition status;

    public InflictStatusFoodItemEffect(int id, int food, int belly, int bellyIfFull, StatusCondition status) {
        super(id, food, belly, bellyIfFull);
        this.status = status;
    }

    @Override
    public void use(ItemUseEvent itemEvent, ArrayList<Event> events) {
        super.use(itemEvent, events);
        events.add(new StatusConditionCreatedEvent(itemEvent.floor, itemEvent,
                this.status.create(itemEvent.floor, itemEvent.target, itemEvent.item)));
    }

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.status.StatusCondition;

public class AbilityStatusOnHit extends AbilityOnHit {

    public final StatusCondition condition;

    public AbilityStatusOnHit(int id, StatusCondition condition, int probability) {
        super(id, probability);
        this.condition = condition;
    }

    @Override
    protected void onHit(Floor floor, DamageDealtEvent event, MoveUse source, TriggeredAbilityEvent abilityEvent,
            ArrayList<DungeonEvent> resultingEvents) {
        resultingEvents.add(new StatusConditionCreatedEvent(floor, event,
                this.condition.create(floor, source.user, abilityEvent.pokemon, floor.random)));
    }

}

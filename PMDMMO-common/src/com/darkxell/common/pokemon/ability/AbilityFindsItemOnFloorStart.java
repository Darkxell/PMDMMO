package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.item.ItemCreatedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityFindsItemOnFloorStart extends Ability {

    public final double probability;

    public AbilityFindsItemOnFloorStart(int id, double probability) {
        super(id);
        this.probability = probability;
    }

    @Override
    public void onFloorStart(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
        super.onFloorStart(floor, pokemon, events);

        if (!pokemon.hasItem() && floor.random.nextDouble() * 100 < this.probability) {
            ItemStack item = floor.dungeon.dungeon().randomItem(floor.random, floor.id, false);
            if (item == null) return;
            TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, DungeonEventSource.TRIGGER, pokemon);
            events.add(abilityevent);
            events.add(new ItemCreatedEvent(floor, abilityevent, item, pokemon));
        }
    }

}

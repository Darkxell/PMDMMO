package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.item.effects.OrbItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusConditions;

public class AbilityTruant extends Ability {

    public AbilityTruant(int id) {
        super(id);
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);

        if (this.shouldTruant(floor, event, concerned, false)) {
            TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, event, concerned);
            resultingEvents
                    .add(new StatusConditionCreatedEvent(floor, abilityevent, StatusConditions.Paused.create(floor, concerned, this)));
        }
    }

    /** @param prevent - If true, method will check as before the event triggers, else as after it does. */
    public boolean shouldTruant(Floor floor, Event event, DungeonPokemon concerned, boolean prevent) {

        if (event instanceof MoveSelectionEvent) {
            MoveSelectionEvent e = (MoveSelectionEvent) event;
            if (e.usedMove().user == concerned && e.isAction() && concerned.ability() == this) return true;
        } else if (event instanceof ItemUseEvent && !prevent) {
            ItemUseEvent e = (ItemUseEvent) event;
            if (e.user == concerned && e.item.effect() instanceof OrbItemEffect && concerned.ability() == this) return true;
        } else if (event instanceof ItemSelectionEvent && prevent) {
            ItemSelectionEvent e = (ItemSelectionEvent) event;
            if (e.user() == concerned && e.item().effect() instanceof OrbItemEffect && e.isAction() && concerned.ability() == this) return true;
        }
        return false;
    }

}

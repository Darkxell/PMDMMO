package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class BoostStatOnHitStatusCondition extends StatusCondition {

    public final Stat stat;

    public BoostStatOnHitStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, Stat stat) {
        super(id, isAilment, durationMin, durationMax);
        this.stat = stat;
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);

        if (event instanceof DamageDealtEvent) {
            DamageDealtEvent e = (DamageDealtEvent) event;
            if (e.target.hasStatusCondition(this) && e.target == concerned) {
                resultingEvents.add(new StatChangedEvent(floor, event, e.target, this.stat, 1));
            }
        }
    }

}

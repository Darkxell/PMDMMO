package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;

public class PeriodicHealingStatusCondition extends StatusCondition {

    /** The damage this Status Condition deals. */
    public final int heal;
    /** The number of turns between each health restored. */
    public final int period;

    public PeriodicHealingStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int heal,
            int period) {
        super(id, isAilment, durationMin, durationMax);
        this.heal = heal;
        this.period = period;
    }

    @Override
    public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<Event> events) {
        if (instance.tick % this.period == 0)
            events.add(new HealthRestoredEvent(floor, instance, instance.pokemon, this.heal));
    }

}

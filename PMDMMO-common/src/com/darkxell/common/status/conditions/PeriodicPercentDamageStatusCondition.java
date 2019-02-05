package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.AppliedStatusCondition;

public class PeriodicPercentDamageStatusCondition extends PeriodicDamageStatusCondition {

    public PeriodicPercentDamageStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int period,
            double damage) {
        super(id, isAilment, durationMin, durationMax, period, damage);
    }

    @Override
    protected int damageDealt(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events) {
        return (int) (instance.pokemon.getMaxHP() * this.damage);
    }

}

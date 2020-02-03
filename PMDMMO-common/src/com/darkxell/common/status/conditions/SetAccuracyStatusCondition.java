package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class SetAccuracyStatusCondition extends StatusCondition {

    public final int accuracy;

    public SetAccuracyStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int accuracy) {
        super(id, isAilment, durationMin, durationMax);
        this.accuracy = accuracy;
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, MoveUseEvent moveEvent, ArrayList<Event> events) {
        if (isUser && move.user.hasStatusCondition(this))
            return this.accuracy;
        return super.applyStatModifications(stat, value, move, target, isUser, floor, moveEvent, events);
    }

}

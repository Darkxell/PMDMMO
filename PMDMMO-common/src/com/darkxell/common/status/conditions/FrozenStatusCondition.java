package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;

public class FrozenStatusCondition extends PreventActionStatusCondition {

    public FrozenStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, MoveUseEvent moveEvent, ArrayList<Event> events) {
        if (stat == Stat.Evasiveness && target.hasStatusCondition(this) && !isUser && !move.move.move().piercesFreeze)
            return 0;
        return super.applyStatModifications(stat, value, move, target, isUser, floor, moveEvent, events);
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);
        if (event instanceof MoveUseEvent) {
            MoveUseEvent e = (MoveUseEvent) event;
            if (!e.missed() && e.target.hasStatusCondition(this) && e.usedMove.move.move().type == PokemonType.Fire) {
                AppliedStatusCondition s = e.target.getStatusCondition(this);
                s.finish(floor, StatusConditionEndReason.BROKEN, event, resultingEvents);
            }
        }
    }

}

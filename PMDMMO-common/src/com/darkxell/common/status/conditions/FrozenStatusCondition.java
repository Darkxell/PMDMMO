package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;

public class FrozenStatusCondition extends PreventActionStatusCondition {

    public FrozenStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveContext context, boolean isUser, ArrayList<Event> events) {
        if (stat == Stat.Evasiveness && context.target.hasStatusCondition(this) && !isUser && !context.move.isPiercesFreeze())
            return 0;
        return super.applyStatModifications(stat, value, context, isUser, events);
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);
        if (event instanceof MoveUseEvent) {
            MoveUseEvent e = (MoveUseEvent) event;
            if (!e.missed() && e.target.hasStatusCondition(this) && e.usedMove.moveType() == PokemonType.Fire) {
                AppliedStatusCondition s = e.target.getStatusCondition(this);
                resultingEvents.add(s.finish(floor, StatusConditionEndReason.BROKEN, event));
            }
        }
    }

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityStatBoost extends Ability {

    public final double multiplier;
    public final Stat stat;

    public AbilityStatBoost(int id, Stat stat, double multiplier) {
        super(id);
        this.stat = stat;
        this.multiplier = multiplier;
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, MoveUseEvent moveEvent, ArrayList<Event> events) {
        if (this.shouldBoost(stat, value, move, target, isUser, floor, events)) {
            events.add(new TriggeredAbilityEvent(floor, moveEvent, move.user));
            return value * this.multiplier;
        }
        return super.applyStatModifications(stat, value, move, target, isUser, floor, moveEvent, events);
    }

    protected boolean shouldBoost(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<Event> events) {
        return stat == this.stat && isUser;
    }

}

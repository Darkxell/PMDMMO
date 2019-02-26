package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class IdentifiedStatusCondition extends RemoveTypeImmunitiesStatusCondition {

    public IdentifiedStatusCondition(int id) {
        super(id, true, -1, -1, PokemonType.Ghost);
    }

    @Override
    public Pair<Boolean, Message> affects(Floor floor, AppliedStatusCondition condition, DungeonPokemon pokemon) {
        Pair<Boolean, Message> p = super.affects(floor, condition, pokemon);
        return new Pair<>(true, p.second);
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, MoveUseEvent moveEvent, ArrayList<Event> events) {
        if (stat == Stat.Evasiveness && target.hasStatusCondition(this) && !isUser)
            return 999; // 999 because great evasion = less likely to evade
        return super.applyStatModifications(stat, value, move, target, isUser, floor, moveEvent, events);
    }

}

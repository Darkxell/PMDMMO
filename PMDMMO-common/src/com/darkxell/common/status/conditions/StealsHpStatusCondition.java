package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class StealsHpStatusCondition extends StatusCondition {

    /** Amount of HP stolen. */
    public final int hp;
    /** Number of turns after which the HP is stolen. */
    public final int turnCycle;

    public StealsHpStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int turnCycle, int hp) {
        super(id, isAilment, durationMin, durationMax);
        this.turnCycle = turnCycle;
        this.hp = hp;
    }

    @Override
    public Pair<Boolean, Message> affects(Floor floor, AppliedStatusCondition condition, DungeonPokemon pokemon) {
        Pair<Boolean, Message> sup = super.affects(floor, condition, pokemon);
        if (!sup.first)
            return sup;
        if (this == StatusConditions.Leech_seed && pokemon.species().isType(PokemonType.Grass))
            return new Pair<>(false, this.immune(pokemon));
        return new Pair<>(true, null);
    }

    @Override
    public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events) {
        super.tick(floor, instance, events);
        if (!(instance.source instanceof DungeonPokemon) || ((DungeonPokemon) instance.source).isFainted())
            instance.finish(floor, StatusConditionEndReason.CANT_CONTINUE, events);
        else if (instance.tick % this.turnCycle == 0) {
            events.add(new DamageDealtEvent(floor, eventSource, instance.pokemon, this, DamageType.CONDITION, this.hp));
            events.add(new HealthRestoredEvent(floor, eventSource, (DungeonPokemon) instance.source, this.hp));
        }
    }

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;
import java.util.Arrays;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.RandomUtil;

public class AbilityEffectSpore extends AbilityOnHit {

    public final StatusCondition[] conditions;

    public AbilityEffectSpore(int id) {
        super(id, 12);
        this.conditions = new StatusCondition[] { StatusConditions.Paralyzed, StatusConditions.Asleep, StatusConditions.Poisoned };
    }

    private boolean isImmune(Floor floor, DungeonPokemon user) {
        return user.species().isType(PokemonType.Grass);
    }

    @Override
    protected void onHit(Floor floor, DamageDealtEvent event, MoveUse source, TriggeredAbilityEvent abilityEvent,
            ArrayList<Event> resultingEvents) {
        if (!this.isImmune(floor, source.user)) {
            StatusCondition condition = RandomUtil.random(Arrays.asList(this.conditions), floor.random);

            for (int i = 0; i < this.conditions.length; ++i)
                if (this.conditions[i] == condition) {
                    abilityEvent.messageID = i + 1;
                    break;
                }

            resultingEvents
                    .add(new StatusConditionCreatedEvent(floor, event, condition.create(floor, source.user, abilityEvent.pokemon, floor.random)));
        }
    }
}

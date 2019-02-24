package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

/** An ability that boosts moves of a certain type when the user has 1/4 of its HP or less. */
public class AbilityTypeBoost extends Ability {

    /** The type of moves to boost. */
    public final PokemonType type;

    public AbilityTypeBoost(int id, PokemonType type) {
        super(id);
        this.type = type;
    }

    @Override
    public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags,
            ArrayList<DungeonEvent> events) {
        if (isUser && move.user.getHpPercentage() < 25 && move.move.move().type == this.type) {
            events.add(new TriggeredAbilityEvent(floor, eventSource, move.user));
            return 2;
        }
        return 1;
    }

}

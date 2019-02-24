package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
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
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        if (isUser && moveEvent.usedMove.user.getHpPercentage() < 25
                && moveEvent.usedMove.move.move().type == this.type) {
            events.add(new TriggeredAbilityEvent(moveEvent.floor, moveEvent, moveEvent.usedMove.user));
            return 2;
        }
        return 1;
    }

}

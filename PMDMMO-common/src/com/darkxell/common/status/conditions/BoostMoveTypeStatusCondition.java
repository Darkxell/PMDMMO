package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;

public class BoostMoveTypeStatusCondition extends StatusCondition {

    public final PokemonType type;

    public BoostMoveTypeStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, PokemonType type) {
        super(id, isAilment, durationMin, durationMax);
        this.type = type;
    }

    @Override
    public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags,
            ArrayList<DungeonEvent> events) {
        if (isUser && move.move.move().type == this.type)
            return 2;
        return super.damageMultiplier(move, target, isUser, floor, flags, events);
    }

}

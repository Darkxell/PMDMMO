package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;

public class BoostMoveTypeStatusCondition extends StatusCondition {

    public final PokemonType type;

    public BoostMoveTypeStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, PokemonType type) {
        super(id, isAilment, durationMin, durationMax);
        this.type = type;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        if (isUser && moveEvent.usedMove.move.move().type == this.type)
            return 2;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

}

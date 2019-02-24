package com.darkxell.common.status.floor;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.FloorStatus;

public class MultiplyTypeFloorStatus extends FloorStatus {

    public final double multiplier;
    public final PokemonType type;

    public MultiplyTypeFloorStatus(int id, int durationMin, int durationMax, PokemonType type, double multiplier) {
        super(id, durationMin, durationMax);
        this.type = type;
        this.multiplier = multiplier;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        if (moveEvent.usedMove.move.move().type == this.type)
            return this.multiplier;
        return super.damageMultiplier(isUser, moveEvent, events);
    }

}

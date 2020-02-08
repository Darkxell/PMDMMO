package com.darkxell.common.status.floor;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
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
    public double damageMultiplier(boolean isUser, MoveContext context, ArrayList<Event> events) {
        if (context.move.getType(context.user.usedPokemon) == this.type)
            return this.multiplier;
        return super.damageMultiplier(isUser, context, events);
    }

}

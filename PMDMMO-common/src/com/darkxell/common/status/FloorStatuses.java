package com.darkxell.common.status;

import java.util.HashMap;

import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.floor.MultiplyTypeFloorStatus;

public final class FloorStatuses {
    static final HashMap<Integer, FloorStatus> _registry = new HashMap<>();

    public static final FloorStatus Reduce_fire = new MultiplyTypeFloorStatus(0, 10, 11, PokemonType.Fire, .5);
    public static final FloorStatus Reduce_electric = new MultiplyTypeFloorStatus(1, 10, 11, PokemonType.Electric, .5);

    /** @return The Floor Status with the input ID. */
    public static FloorStatus find(int id) {
        return _registry.get(id);
    }

    private FloorStatuses() {
    }

}

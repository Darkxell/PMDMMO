package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventsStatLoss extends AbilityPreventsAnyStatLoss {

    private final Stat[] protectedStats;

    public AbilityPreventsStatLoss(int id, Stat... protectedStats) {
        super(id);
        this.protectedStats = protectedStats;
    }

    @Override
    protected boolean isPrevented(Floor floor, StatChangedEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        for (Stat s : this.protectedStats)
            if (s == event.stat)
                return super.isPrevented(floor, event, concerned, resultingEvents);
        return false;
    }

}

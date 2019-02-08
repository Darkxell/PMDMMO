package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;

public class MultiplyWeightEffect extends MoveEffect {

    private final int[][] multiplierTable = new int[][] { { 0, 20, 30, 40, 50, 60, 70, 80, 200 },
            { 60, 70, 80, 90, 100, 110, 120, 130, 140 } };

    public MultiplyWeightEffect(int id) {
        super(id);
    }

    @Override
    public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags,
            ArrayList<DungeonEvent> events) {
        float weight = target.species().weight;
        for (int i = this.multiplierTable.length - 1; i >= 0; --i)
            if (weight >= this.multiplierTable[i][0])
                return this.multiplierTable[i][1] * 1. / 100;
        return super.damageMultiplier(move, target, isUser, floor, flags, events);
    }

}

package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class UserLevelDamageCalculator extends MoveEffectCalculator {

    public UserLevelDamageCalculator(MoveUse move, DungeonPokemon target, Floor floor, String[] flags) {
        super(move, target, floor, flags);
    }

    @Override
    public int compute(ArrayList<DungeonEvent> events) {
        return move.user.level();
    }

}

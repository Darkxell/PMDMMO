package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.CantMissCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class CantMissEffect extends MoveEffect {

    public CantMissEffect(int id) {
        super(id);
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags) {
        return new CantMissCalculator(usedMove, target, floor, flags);
    }

}

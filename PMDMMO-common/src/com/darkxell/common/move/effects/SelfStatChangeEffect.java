package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class SelfStatChangeEffect extends StatChangeEffect {

    public SelfStatChangeEffect(int id, Stat stat, int stage, int probability) {
        super(id, stat, stage, probability);
    }

    @Override
    protected DungeonPokemon pokemonToChange(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        return moveEvent.usedMove.user;
    }

}

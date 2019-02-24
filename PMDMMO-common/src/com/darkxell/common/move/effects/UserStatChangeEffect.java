package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class UserStatChangeEffect extends StatChangeEffect {

    public UserStatChangeEffect(int id, Stat stat, int stage, int probability) {
        super(id, stat, stage, probability);
    }

    @Override
    protected String descriptionID() {
        return super.descriptionID().replaceAll("stat", "stat_user");
    }

    @Override
    protected DungeonPokemon pokemonToChange(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        return usedMove.user;
    }

}

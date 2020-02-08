package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class UserStatChangeEffect extends StatChangeEffect {

    public UserStatChangeEffect(Stat stat, int stage, int probability) {
        super(stat, stage, probability);
    }

    @Override
    protected String descriptionID() {
        return super.descriptionID().replaceAll("stat", "stat_user");
    }

    @Override
    protected DungeonPokemon pokemonToChange(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        return moveEvent.usedMove.user;
    }

}

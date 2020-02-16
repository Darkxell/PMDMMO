package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class SelfStatChangeEffect extends StatChangeEffect {

    public SelfStatChangeEffect(Stat stat, int stage, int probability) {
        super(stat, stage, probability);
    }

    @Override
    protected DungeonPokemon pokemonToChange(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        return context.user;
    }

}

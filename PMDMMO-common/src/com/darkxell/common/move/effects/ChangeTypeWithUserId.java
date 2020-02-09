package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;

public class ChangeTypeWithUserId extends MoveEffect {

    @Override
    public PokemonType alterMoveType(Move move, Pokemon user) {
        if (user == null)
            return null;
        return PokemonType.values()[((int) Math.abs(user.id())) % PokemonType.values().length];
    }

    @Override
    public void effects(MoveContext moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}

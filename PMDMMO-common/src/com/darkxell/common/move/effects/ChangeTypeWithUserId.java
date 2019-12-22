package com.darkxell.common.move.effects;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;

public class ChangeTypeWithUserId extends MoveEffect {

    public ChangeTypeWithUserId(int id) {
        super(id);
    }

    @Override
    public PokemonType getMoveType(Move move, Pokemon user) {
        if (user == null)
            return move.getType();
        return PokemonType.values()[((int) Math.abs(user.id())) % PokemonType.values().length];
    }

}

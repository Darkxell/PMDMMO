package com.darkxell.client.resources.images.pokemon;

import javafx.util.Pair;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;

public class PokemonSpriteset extends RegularSpriteSet {

    public final PokemonSpritesetData data;

    protected PokemonSpriteset(String path, PokemonSpritesetData data) {
        super(path, data.spriteWidth, data.spriteHeight, -1, -1);
        this.data = data;
    }

    public PokemonSpriteFrame getFrame(PokemonSpriteState state, Direction direction, int tick) {
        return this.getSequence(state, direction).getFrame(tick);
    }

    public PokemonSpriteSequence getSequence(PokemonSpriteState state, Direction direction) {
        Integer index = this.data.states.get(new Pair<>(state, direction));
        if (index == null)
            index = this.data.states.get(new Pair<>(PokemonSpriteState.IDLE, direction));
        return this.data.sequences.get(index);
    }

}

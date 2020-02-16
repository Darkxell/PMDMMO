package com.darkxell.client.resources.image.pokemon.body;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Pair;

public class PokemonSpriteset extends PMDRegularSpriteset {

    public final PokemonSpritesetData data;

    protected PokemonSpriteset(String path, PokemonSpritesetData data) {
        super(path, data.spriteWidth, data.spriteHeight, -1, -1);
        this.data = data;
    }

    public PSDFrame getFrame(PokemonSpriteState state, Direction direction, int tick) {
        return this.getSequence(state, direction).getFrame(tick);
    }

    public PSDSequence getSequence(PokemonSpriteState state, Direction direction) {
        Integer index = this.data.states.get(new Pair<>(state, direction));
        if (index == null)
            index = this.data.states.get(new Pair<>(PokemonSpriteState.IDLE, direction));
        if (index == null) {
            Logger.e("Error while rendering. Sprite stats are mismapped.");
            index = 0;
        }
        return this.data.sequences.get(index);
    }

}

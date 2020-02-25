package com.darkxell.client.mechanics.animation.misc;

import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.client.renderers.RenderOffset;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.image.tileset.dungeon.DungeonTerrainTileset;

public class ImmobilizedAnimation extends PokemonAnimation implements RenderOffset {

    public ImmobilizedAnimation(AnimationVariantModel data, AbstractPokemonRenderer renderer,
            AnimationEndListener listener) {
        super(data, renderer, 3, listener);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        this.renderer.unregisterOffset(this);
    }

    @Override
    public void start() {
        super.start();
        this.renderer.registerOffset(this);
    }

    @Override
    public double xOffset() {
        return (this.tick() / 2 % 3 - 1) * 1. / DungeonTerrainTileset.TILE_SIZE;
    }

    @Override
    public double yOffset() {
        return 0;
    }

}

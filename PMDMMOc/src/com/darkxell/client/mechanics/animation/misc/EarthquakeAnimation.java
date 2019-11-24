package com.darkxell.client.mechanics.animation.misc;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.RenderOffset;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.image.tileset.dungeon.AbstractDungeonTileset;

public class EarthquakeAnimation extends PokemonAnimation implements RenderOffset {

    public static final int DURATION = 60;

    public EarthquakeAnimation(AnimationData data, AbstractPokemonRenderer renderer, AnimationEndListener listener) {
        super(data, renderer, DURATION, listener);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Persistence.dungeonState.floorRenderer.unregisterOffset(this);
        Persistence.dungeonState.itemRenderer.unregisterOffset(this);
        Persistence.dungeonState.shadowRenderer.unregisterOffset(this);
    }

    @Override
    public void start() {
        super.start();
        Persistence.dungeonState.floorRenderer.registerOffset(this);
        Persistence.dungeonState.itemRenderer.registerOffset(this);
        Persistence.dungeonState.shadowRenderer.registerOffset(this);
    }

    @Override
    public double xOffset() {
        return 0;
    }

    @Override
    public double yOffset() {
        return Math.sin(this.tick() * 1. / 2) / (this.tick() * 1. / 6) * AbstractDungeonTileset.TILE_SIZE / 2;
    }

}

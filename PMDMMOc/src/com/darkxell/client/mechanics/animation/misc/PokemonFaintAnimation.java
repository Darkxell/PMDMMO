package com.darkxell.client.mechanics.animation.misc;

import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.model.animation.AnimationVariantModels.DefaultVariant;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;

public class PokemonFaintAnimation extends PokemonAnimation {
    public static final int DURATION = 30;

    public PokemonFaintAnimation(AbstractPokemonRenderer renderer, AnimationEndListener listener) {
        super(new DefaultVariant(), renderer, DURATION, listener);
    }

    @Override
    public void onFinish() {
        this.renderer.sprite().setDefaultState(PokemonSpriteState.IDLE, true);
        super.onFinish();
    }

    @Override
    public void start() {
        super.start();
        this.renderer.sprite().setDefaultState(PokemonSpriteState.HURT, true);
    }

    @Override
    public void update() {
        super.update();
        this.renderer.setAlpha(1 - this.completion());
    }

}

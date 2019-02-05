package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.graphics.renderer.AbstractPokemonRenderer;
import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;

/** An animation that is displayed on a Pokemon. */
public class PokemonAnimation extends AbstractAnimation {

    /** Describes the movement of the Pokemon during this Animation. May be null if the Pokemon doesn't move. */
    public PokemonAnimationMovement movement;
    /** A reference to the Pokemon's renderer. */
    public final AbstractPokemonRenderer renderer;
    /** Coordinates of the center of the Pokemon. */
    protected double x, y;

    public PokemonAnimation(AnimationData data, AbstractPokemonRenderer renderer, int duration,
            AnimationEndListener listener) {
        super(data, duration, listener);
        this.renderer = renderer;
    }

    @Override
    public void onFinish() {
        if (this.movement != null)
            this.movement.onFinish();
        if (this.renderer != null)
            this.renderer.removeAnimation(this);
        super.onFinish();
    }

    /** Rendering done after the Pokemon is drawn. */
    public void postrender(Graphics2D g, int width, int height) {
    }

    /** Rendering done before the Pokemon is drawn. */
    public void prerender(Graphics2D g, int width, int height) {
    }

    @Override
    @Deprecated
    public void render(Graphics2D g, int width, int height) {
        if (this.renderer == null)
            this.postrender(g, width, height);
    }

    @Override
    public void start() {
        super.start();
        if (this.renderer != null)
            this.renderer.addAnimation(this);
        if (this.data.pokemonState != null && this.data.pokemonStateDelay == 0 && this.renderer != null)
            this.renderer.sprite().setState(this.data.pokemonState);
        if (this.movement != null)
            this.movement.start();
    }

    @Override
    public void update() {
        super.update();
        if (this.data.pokemonState != null && this.tick() == this.data.pokemonStateDelay)
            this.renderer.sprite().setState(this.data.pokemonState);
        if (this.renderer != null) {
            this.x = this.renderer.drawX();
            this.y = this.renderer.drawY();
        }
        if (this.movement != null)
            this.movement.update();
    }

}

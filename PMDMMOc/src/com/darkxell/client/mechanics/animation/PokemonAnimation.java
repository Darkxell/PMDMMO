package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;


/** An animation that is displayed on a Pokemon. */
public class PokemonAnimation extends AbstractAnimation {

    /** Describes the movement of the Pokemon during this Animation. May be null if the Pokemon doesn't move. */
    public PokemonAnimationMovement movement;
    /** A reference to the Pokemon's renderer. */
    public final AbstractPokemonRenderer renderer;
    /** Coordinates of the center of the Pokemon. */
    protected double x, y;

    public PokemonAnimation(AnimationVariantModel model, AbstractPokemonRenderer renderer, int duration,
            AnimationEndListener listener) {
        super(model, duration, listener);
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
        if (this.data.getPokemonState() != null && this.data.getPokemonStateDelay() == 0 && this.renderer != null)
            this.renderer.sprite().setState(this.data.getPokemonState());
        if (this.movement != null)
            this.movement.start();
    }

    @Override
    public void update() {
        super.update();
        if (this.data.getPokemonState() != null && this.tick() == this.data.getPokemonStateDelay())
            this.renderer.sprite().setState(this.data.getPokemonState());
        if (this.renderer != null) {
            this.x = this.renderer.drawX();
            this.y = this.renderer.drawY();
        }
        if (this.movement != null)
            this.movement.update();
    }

}

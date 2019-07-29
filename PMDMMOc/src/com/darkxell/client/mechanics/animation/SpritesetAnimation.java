package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.spritemovement.SpritesetAnimationMovement;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;

public class SpritesetAnimation extends PokemonAnimation {

    public enum BackSpriteUsage {
        /** The sprites should be drawn behind the Pokemon. */
        no,
        /** The sprites should be drawn above the Pokemon. */
        only,
        /** There are sprites behind and above the Pokemon. */
        yes
    }

    public final int[] sprites;
    public final PMDRegularSpriteset spriteset;
    /** Describes how the Sprites may move. May be null for no Movement. */
    SpritesetAnimationMovement spritesetMovement;

    public SpritesetAnimation(AnimationData data, AbstractPokemonRenderer renderer, PMDRegularSpriteset spriteset,
            int[] sprites, AnimationEndListener listener) {
        super(data, renderer, sprites.length * data.spriteDuration, listener);
        this.spriteset = spriteset;
        this.sprites = sprites;
    }

    private void draw(Graphics2D g, boolean back) {
        int index = this.index();
        if (index != -1 && back && this.data.backSpriteUsage == BackSpriteUsage.yes)
            index += this.spriteset.spriteCount() / 2;

        if (index != -1 && ((back && this.data.backSpriteUsage != BackSpriteUsage.no)
                || (!back && this.data.backSpriteUsage != BackSpriteUsage.only)))
            g.drawImage(this.spriteset.getSprite(index), (int) this.x - this.data.gravityX,
                    (int) (this.y - this.data.gravityY), null);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int index() {
        if (this.sprites.length == 0)
            return -1;
        int i = this.tick() / this.data.spriteDuration;
        while (i >= this.sprites.length && this.plays != 1)
            i -= this.sprites.length - this.data.loopsFrom;
        if (i >= this.sprites.length)
            return -1;
        return this.sprites[i];
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (this.spritesetMovement != null)
            this.spritesetMovement.onFinish();
    }

    @Override
    public void postrender(Graphics2D g, int width, int height) {
        super.postrender(g, width, height);
        this.draw(g, false);
    }

    @Override
    public void prerender(Graphics2D g, int width, int height) {
        super.prerender(g, width, height);
        this.draw(g, true);
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void start() {
        super.start();
        if (this.spritesetMovement != null)
            this.spritesetMovement.start();
    }

    @Override
    public void update() {
        super.update();
        if (this.spritesetMovement != null)
            this.spritesetMovement.update();
    }

}

package com.darkxell.client.mechanics.animation.misc;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.OverlayAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;

public class SnowAnimation extends AbstractAnimation {

    public static final int DURATION = 64;

    private SpritesetAnimation[] snow;

    public SnowAnimation(AnimationEndListener listener) {
        super(new AnimationData(-1), DURATION, listener);
        this.data.sound = "weather-snow";
    }

    @Override
    public boolean isOver() {
        return this.snow != null && this.snow[0].isOver();
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);
        if (this.snow == null) {
            SpritesetAnimation a = (SpritesetAnimation) Animations.getCustomAnimation(null, 102, null);
            int cols = width / a.spriteset.spriteWidth, lines = height / a.spriteset.spriteHeight;
            if (width % a.spriteset.spriteWidth != 0)
                ++cols;
            if (height % a.spriteset.spriteHeight != 0)
                ++lines;
            this.snow = new SpritesetAnimation[lines * cols];
            for (int x = 0; x < cols; ++x)
                for (int y = 0; y < lines; ++y) {
                    this.snow[x + y * cols] = (SpritesetAnimation) Animations.getCustomAnimation(null, 102, null);
                    this.snow[x + y * cols].setXY(x * a.spriteset.spriteWidth, y * a.spriteset.spriteHeight);
                }
        }

        float alpha = this.tick() < OverlayAnimation.FADE ? this.tick() * 1f / OverlayAnimation.FADE
                : this.tick() >= DURATION - OverlayAnimation.FADE
                        ? (DURATION - this.tick()) * 1f / OverlayAnimation.FADE
                        : 1;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        Composite c = g.getComposite();
        if (alpha != 1)
            g.setComposite(ac);
        for (SpritesetAnimation animation : this.snow)
            animation.postrender(g, width, height);
        if (alpha != 1)
            g.setComposite(c);
    }

    @Override
    public void update() {
        super.update();
        if (this.snow != null)
            for (SpritesetAnimation animation : this.snow)
                animation.update();
    }

}

package com.darkxell.client.mechanics.animation.misc;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.client.model.animation.AnimationVariantModels.DefaultVariant;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.state.mainstates.PrincipalMainState;

public class WindAnimation extends PokemonAnimation {

    public static final int DURATION = 60;

    private ArrayList<SpritesetAnimation> particles;

    public WindAnimation(int particleAnimationID, int particleCount, AnimationVariantModel data,
            AbstractPokemonRenderer renderer, AnimationEndListener listener) {
        super(new DefaultVariant(), renderer, DURATION, listener);
        this.particles = new ArrayList<>();
        for (int i = 0; i < particleCount; ++i) {
            this.createParticle(particleAnimationID);
        }
    }

    private void createParticle(int particleAnimationID) {
        int baseX = (int) this.renderer.drawX(), baseY = (int) this.renderer.drawY();
        int x = baseX - PrincipalMainState.displayWidth / 2, y = baseY - PrincipalMainState.displayHeight / 2;

        SpritesetAnimation a = (SpritesetAnimation) Animations.getCustomAnimation(null, particleAnimationID, null);
        a.setXY(x - Math.random() * PrincipalMainState.displayWidth - a.spriteset.spriteWidth,
                y + Math.random() * PrincipalMainState.displayHeight - a.spriteset.spriteHeight / 2);
        a.data.setLoopsFrom(0);
        a.plays = -1;
        this.particles.add(a);
    }

    @Override
    public void postrender(Graphics2D g, int width, int height) {
        for (SpritesetAnimation animation : this.particles) {
            animation.postrender(g, width, height);
        }
    }

    @Override
    public void update() {
        super.update();

        int speed = (int) (PrincipalMainState.displayWidth * 2 / DURATION);

        for (SpritesetAnimation animation : this.particles) {
            animation.update();
            animation.setXY(animation.getX() + speed, animation.getY());
        }
    }

}

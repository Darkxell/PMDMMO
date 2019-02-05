package com.darkxell.client.mechanics.animation;

import java.util.ArrayList;

public final class AnimationTicker {

    public static final AnimationTicker instance = new AnimationTicker();

    private ArrayList<AbstractAnimation> animations = new ArrayList<AbstractAnimation>();

    private AnimationTicker() {
    }

    public void register(AbstractAnimation animation) {
        this.animations.add(animation);
    }

    public void unregister(AbstractAnimation animation) {
        this.animations.remove(animation);
    }

    public void update() {
        for (int i = 0; i < this.animations.size(); ++i)
            this.animations.get(i).update();
    }

}

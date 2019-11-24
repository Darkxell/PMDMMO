package com.darkxell.client.renderers.floor;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;

public class StaticAnimationsRenderer extends AbstractRenderer {

    private ArrayList<AbstractAnimation> animations = new ArrayList<>();

    public StaticAnimationsRenderer() {
        super(0, 0, MasterDungeonRenderer.LAYER_STATIC_ANIMATIONS);
    }

    public void add(AbstractAnimation animation) {
        this.animations.add(animation);
    }

    public void remove(AbstractAnimation animation) {
        this.animations.remove(animation);
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        g.translate(this.drawX(), this.drawY());
        for (AbstractAnimation a : this.animations)
            a.render(g, width, height);
        g.translate(-this.drawX(), -this.drawY());
    }

    @Override
    public void update() {
        super.update();
        for (int i = 0; i < this.animations.size(); ++i)
            if (this.animations.get(i).isOver()) {
                this.animations.remove(i);
                --i;
            }
    }

}

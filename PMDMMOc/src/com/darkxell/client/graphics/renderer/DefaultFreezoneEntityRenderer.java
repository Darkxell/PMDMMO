package com.darkxell.client.graphics.renderer;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezone.entity.FreezoneEntity;
import com.darkxell.client.graphics.AbstractRenderer;
import com.darkxell.common.util.DoubleRectangle;

import java.awt.*;

public class DefaultFreezoneEntityRenderer extends AbstractRenderer {
    public final FreezoneEntity entity;

    public DefaultFreezoneEntityRenderer(FreezoneEntity entity) {
        this.entity = entity;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        this.entity.print(g);
        if (Persistence.debugdisplaymode) {
            g.setColor(new Color(20, 20, 200, 160));
            DoubleRectangle dbrct = this.entity.getHitbox(this.entity.getX(), this.entity.getY());
            g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
        }
    }

    @Override
    public void update() {
        this.setXY(this.entity.getX() * 8, this.entity.getY() * 8);
        super.update();
    }
}

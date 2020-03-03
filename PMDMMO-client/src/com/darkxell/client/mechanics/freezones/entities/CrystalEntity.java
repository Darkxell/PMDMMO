package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;
import java.awt.image.RescaleOp;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.image.Sprites.FreezoneEntitySprites;

public class CrystalEntity extends FreezoneEntity {

    private float color = 0.25f;
    private boolean colordirection = false;

    private float light = 2f;
    private boolean lightdirection = false;

    public CrystalEntity(double x, double y) {
        super(true, true, x, y);
    }

    @Override
    public void onInteract() {

    }

    @Override
    public void print(Graphics2D g) {
        // Draws the cristal
        g.drawImage(FreezoneEntitySprites.cristals.yellow(), (int) (super.posX * 8 - 30), (int) (super.posY * 8 - 75),
                null);
        float[] scales = { 1f, 1f, 1f, color };
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);
        g.drawImage(FreezoneEntitySprites.cristals.red(), rop, (int) (super.posX * 8 - 30),
                (int) (super.posY * 8 - 75));
        // Draws the light beam
        int l = (int) light;
        for (int i = 0; i < l; ++i)
            g.drawImage(FreezoneEntitySprites.cristals.lightray(), (int) (super.posX * 8 - 24),
                    (int) (super.posY * 8 - 258) + (3 * i), null);
    }

    @Override
    public void update() {
        // Cristal color iteration
        if (colordirection)
            color += 0.01;
        else
            color -= 0.01;
        if (color <= 0.05 || color >= 0.95)
            colordirection = !colordirection;
        // Light beam iteration
        if (lightdirection)
            light += 0.12;
        else
            light -= 0.12;
        if (light <= 0.3 || light >= 15)
            lightdirection = !lightdirection;
    }

}

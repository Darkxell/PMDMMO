package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.Sprites.Res_FreezoneEntities;

import java.awt.*;
import java.awt.image.RescaleOp;

class CrystalEntity extends FreezoneEntity {
    private float color = 0.25f;
    private boolean colorDirection = false;

    private float light = 2f;
    private boolean lightDirection = false;

    CrystalEntity() {
        super();
        this.isSolid = true;
        this.canInteract = true;
    }

    @Override
    public void print(Graphics2D g) {
        // Draws the cristal
        g.drawImage(Res_FreezoneEntities.cristal_yellow.image(), (int) (super.posX * 8 - 30),
                (int) (super.posY * 8 - 75), null);
        float[] scales = {1f, 1f, 1f, color};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);
        g.drawImage(Res_FreezoneEntities.cristal_red.image(), rop, (int) (super.posX * 8 - 30),
                (int) (super.posY * 8 - 75));
        // Draws the light beam
        int l = (int) light;
        for (int i = 0; i < l; ++i) {
            g.drawImage(Res_FreezoneEntities.cristal_lightray.image(), (int) (super.posX * 8 - 24),
                    (int) (super.posY * 8 - 258) + (3 * i), null);
        }
    }

    @Override
    public void update() {
        // Cristal color iteration
        color += colorDirection ? 0.01 : -0.01;
        if (color <= 0.05 || color >= 0.95) {
            colorDirection = !colorDirection;
        }

        // Light beam iteration
        light += lightDirection ? 0.12 : -0.12;
        if (light <= 0.3 || light >= 15) {
            lightDirection = !lightDirection;
        }
    }
}

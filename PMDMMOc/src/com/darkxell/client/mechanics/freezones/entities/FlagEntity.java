package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.Sprites.Res_FreezoneEntities;

import java.awt.*;

public class FlagEntity extends FreezoneEntity {
    private byte state = 0;
    private byte counter = 0;

    @Override
    public void print(Graphics2D g) {
        g.drawImage(Res_FreezoneEntities.flag.getImg(this.state), (int) (super.posX * 8), (int) (super.posY * 8), null);
    }

    @Override
    public void update() {
        this.counter = (byte) ((this.counter + 1) % 10);
        if (this.counter == 0) {
            this.state = (byte) ((this.state + 1) % 5);
        }
    }
}

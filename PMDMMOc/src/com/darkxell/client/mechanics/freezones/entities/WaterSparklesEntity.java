package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.Sprites.Res_FreezoneEntities;
import org.jdom2.Element;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WaterSparklesEntity extends FreezoneEntity {
    private String type;
    private byte state = 0;
    private byte counter = 0;


    @Override
    protected void onInitialize(Element el) {
        super.onInitialize(el);

        this.type = el.getAttributeValue("sprite");
    }

    @Override
    public void print(Graphics2D g) {
        g.drawImage(getSprite(), (int) (super.posX * 8), (int) (super.posY * 8), null);
    }

    @Override
    public void update() {
        this.counter = (byte) ((this.counter + 1) % 10);
        if (this.counter == 0) {
            this.state = (byte) ((this.state + 1) % 5);
        }
    }

    private BufferedImage getSprite() {
        switch (this.type) {
            case "side":
                return Res_FreezoneEntities.waterSparkles.side(this.state);
            case "bottom":
                return Res_FreezoneEntities.waterSparkles.bot(this.state);
            case "top":
                return Res_FreezoneEntities.waterSparkles.top(this.state);
            case "long":
                return Res_FreezoneEntities.waterSparkles.lon(this.state);
            default:
                return null;
        }
    }
}

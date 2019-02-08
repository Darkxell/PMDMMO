package com.darkxell.client.mechanics.freezone.entity;

import java.awt.Graphics2D;

import org.jdom2.Element;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.Sprites.Res_FreezoneEntities;
import com.darkxell.client.resources.images.tilesets.AbstractFreezoneTileset;
import com.darkxell.common.util.xml.XMLUtils;

class AnimatedFlowerEntity extends FreezoneEntity {
    private RegularSpriteSet spriteSet;
    private byte state;
    private byte counter = 0;

    @Override
    protected void deserialize(Element el) {
        super.deserialize(el);

        boolean color = XMLUtils.getAttribute(el, "color", false);
        this.spriteSet = color ? Res_FreezoneEntities.YellowFlower : Res_FreezoneEntities.RedFlower;
        this.state = (byte) (color ? 0 : 4);
    }

    @Override
    public void print(Graphics2D g) {
        g.drawImage(this.spriteSet.getImg(this.state), (int) (super.posX * AbstractFreezoneTileset.TILE_SIZE - 16),
                (int) (super.posY * AbstractFreezoneTileset.TILE_SIZE - 24), null);
    }

    @Override
    public void update() {
        this.counter = (byte) ((this.counter + 1) % 10);
        if (this.counter == 0)
            this.state = (byte) ((this.state + 1) % 5);
    }
}

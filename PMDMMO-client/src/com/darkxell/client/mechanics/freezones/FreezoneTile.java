package com.darkxell.client.mechanics.freezones;

import java.awt.image.BufferedImage;

import com.darkxell.client.model.freezone.FreezoneTileModel;

/**
 * A typical 8*8 tile found in freezones. Freezones are the areas where you can move freely and don't have to fight.
 */
public class FreezoneTile {

    private final FreezoneTileModel model;

    public FreezoneTile(FreezoneTileModel model) {
        this.model = model;
    }

    public BufferedImage getSprite(FreezoneMap map) {
        return map.getTileset(this.model.getTileset()).getSprite(this.model.getXo(), this.model.getYo());
    }

    public boolean isSolid() {
        return this.model.getSolid();
    }

}

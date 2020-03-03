package com.darkxell.client.resources.image.dungeon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;

public class ItemsSpriteset extends PMDRegularSpriteset {

    public static final int ITEM_SPRITE_SIZE = 16;

    public ItemsSpriteset() {
        super("/tilesets/items.png", ITEM_SPRITE_SIZE, ITEM_SPRITE_SIZE, 16, 16);
    }

    public BufferedImage getSprite(Item item) {
        return this.getSprite(item.getSpriteID());
    }

    public BufferedImage sprite(ItemStack item) {
        return this.getSprite(item.item());
    }

}

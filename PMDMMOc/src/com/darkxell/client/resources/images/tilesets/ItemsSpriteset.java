package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;

public class ItemsSpriteset extends AbstractTileset
{

	public static ItemsSpriteset instance = new ItemsSpriteset();

	public static final int ITEM_SIZE = 16;

	public ItemsSpriteset()
	{
		super("/tilesets/items.png", ITEM_SIZE, ITEM_SIZE);
	}

	public BufferedImage sprite(Item item)
	{
		return this.SPRITES[item.getSpriteID()];
	}

	public BufferedImage sprite(ItemStack item)
	{
		return this.sprite(item.item());
	}

}

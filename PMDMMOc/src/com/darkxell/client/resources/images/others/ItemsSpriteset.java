package com.darkxell.client.resources.images.others;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;

public class ItemsSpriteset extends RegularSpriteSet
{

	public static final int ITEM_SIZE = 16;

	public ItemsSpriteset()
	{
		super("/tilesets/items.png", ITEM_SIZE, 256, 256);
	}

	public BufferedImage sprite(Item item)
	{
		return this.getImg(item.spriteID);
	}

	public BufferedImage sprite(ItemStack item)
	{
		return this.sprite(item.item());
	}

}

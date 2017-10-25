package com.darkxell.client.resources.images.tilesets;

public class ItemsSpriteset extends AbstractTileset
{

	public static final int ITEM_SIZE = 16;
	
	public static ItemsSpriteset instance = new ItemsSpriteset();

	public ItemsSpriteset()
	{
		super("/tilesets/items.png", ITEM_SIZE, ITEM_SIZE);
	}

}

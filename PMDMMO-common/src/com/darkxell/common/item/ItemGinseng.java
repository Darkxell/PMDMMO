package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.Message;

/** An Item that Increases the power of selected move by 1, and rarely 3. */
public class ItemGinseng extends ItemFood
{

	public ItemGinseng(Element xml)
	{
		super(xml);
	}

	public ItemGinseng(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, sprite, isStackable, food, bellyIfFull, belly);
	}

	public ItemCategory category()
	{
		return ItemCategory.DRINKS;
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.ingest");
	}

}

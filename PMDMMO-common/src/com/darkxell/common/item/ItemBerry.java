package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that cures status effects when eaten. */
public class ItemBerry extends ItemFood
{
	// public final int effect;

	public ItemBerry(Element xml)
	{
		super(xml);
	}

	public ItemBerry(int id, int price, int sell, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, food, bellyIfFull, belly);
	}

}

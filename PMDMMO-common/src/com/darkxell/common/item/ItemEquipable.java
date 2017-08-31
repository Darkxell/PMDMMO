package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that has an effect when hold. */
public class ItemEquipable extends Item
{

	public ItemEquipable(Element xml)
	{
		super(xml);
	}

	public ItemEquipable(int id, int price, int sell, int sprite, boolean isStackable)
	{
		super(id, price, sell, sprite, isStackable);
	}

}

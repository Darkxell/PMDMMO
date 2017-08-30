package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that has different effects when used. */
public class ItemOrb extends Item
{

	public ItemOrb(Element xml)
	{
		super(xml);
	}

	public ItemOrb(int id, int price, int sell, int sprite)
	{
		super(id, price, sell, sprite);
	}

}

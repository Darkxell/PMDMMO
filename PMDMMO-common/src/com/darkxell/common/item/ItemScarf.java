package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that has an effect when hold. */
public class ItemScarf extends Item
{

	public ItemScarf(Element xml)
	{
		super(xml);
	}

	public ItemScarf(int id, int price, int sell)
	{
		super(id, price, sell);
	}

}

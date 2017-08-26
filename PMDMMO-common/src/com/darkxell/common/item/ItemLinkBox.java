package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that allows to link moves in a dungeon. */
public class ItemLinkBox extends Item
{

	public ItemLinkBox(Element xml)
	{
		super(xml);
	}

	public ItemLinkBox(int id, int price, int sell)
	{
		super(id, price, sell);
	}

}

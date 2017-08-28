package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that teaches a move to a Pok�mon when used, then turns into a Used TM. */
public class ItemTM extends ItemHM
{

	public ItemTM(Element xml)
	{
		super(xml);
	}

	public ItemTM(int id, int price, int sell, int moveID)
	{
		super(id, price, sell, moveID);
	}

}

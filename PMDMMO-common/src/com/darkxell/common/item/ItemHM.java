package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that teaches a move to a Pok�mon when used. */
public class ItemHM extends Item
{

	/** The move this TM teaches. */
	public final int moveID;

	public ItemHM(Element xml)
	{
		super(xml);
		this.moveID = Integer.parseInt(xml.getAttributeValue("move"));
	}

	public ItemHM(int id, int price, int sell, int moveID)
	{
		super(id, price, sell);
		this.moveID = moveID;
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("move", Integer.toString(this.moveID));
	}

}

package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that increases a stat when drunk. */
public class ItemDrink extends ItemFood
{

	public final byte stat;

	public ItemDrink(Element xml)
	{
		super(xml);
		this.stat = Byte.parseByte(xml.getAttributeValue("stat"));
	}

	public ItemDrink(int id, int price, int sell, int sprite, int food, int bellyIfFull, int belly, byte stat)
	{
		super(id, price, sell, sprite, food, bellyIfFull, belly);
		this.stat = stat;
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("stat", Byte.toString(this.stat));
	}

}

package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that boosts a stat when hold. */
public class ItemStatEquipable extends Item
{

	/** The boosted stat. */
	public final byte stat;

	public ItemStatEquipable(Element xml)
	{
		super(xml);
		this.stat = Byte.parseByte(xml.getAttributeValue("stat"));
	}

	public ItemStatEquipable(int id, int price, int sell, byte stat)
	{
		super(id, price, sell);
		this.stat = stat;
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("stat", Byte.toString(this.stat));
	}

}

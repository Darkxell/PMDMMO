package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that restores PP when eaten. */
public class ItemElixir extends ItemFood
{

	public final int pp;

	public ItemElixir(Element xml)
	{
		super(xml);
		this.pp = Integer.parseInt(xml.getAttributeValue("pp"));
	}

	public ItemElixir(int id, int price, int sell, int food, int bellyIfFull, int belly, int pp)
	{
		super(id, price, sell, food, bellyIfFull, belly);
		this.pp = pp;
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("pp", Integer.toString(this.pp));
	}

}

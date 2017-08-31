package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that restores belly when eaten, and inflicts negative status effects. */
public class ItemHeal extends ItemFood
{

	/** The amount of health this Item restores when eaten. */
	public final int hp;
	/** The amount of health this Item adds to the maximum health when eaten with full health. */
	public final int hpFull;

	public ItemHeal(Element xml)
	{
		super(xml);
		this.hp = xml.getAttribute("hp") == null ? 0 : Integer.parseInt(xml.getAttributeValue("hp"));
		this.hpFull = xml.getAttribute("hp-full") == null ? 0 : Integer.parseInt(xml.getAttributeValue("hp-full"));
	}

	public ItemHeal(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly, int hp, int hpFull)
	{
		super(id, price, sell, sprite, isStackable, food, bellyIfFull, belly);
		this.hp = hp;
		this.hpFull = hpFull;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (this.hp != 0) root.setAttribute("hp", Integer.toString(this.hp));
		if (this.hpFull != 0) root.setAttribute("hp-full", Integer.toString(this.hpFull));
		return root;
	}

}

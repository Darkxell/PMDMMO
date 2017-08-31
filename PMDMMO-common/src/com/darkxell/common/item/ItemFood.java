package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that restores belly when eaten. */
public class ItemFood extends Item
{

	/** The increase of belly size given by this Food when eaten. */
	public final int belly;
	/** The increase of belly size given by this Food when eaten while the belly is full. */
	public final int bellyIfFull;
	/** The amount of food given by this Food when eaten. */
	public final int food;

	public ItemFood(Element xml)
	{
		super(xml);
		this.food = xml.getAttribute("food") == null ? 0 : Integer.parseInt(xml.getAttributeValue("food"));
		this.bellyIfFull = xml.getAttribute("full") == null ? 0 : Integer.parseInt(xml.getAttributeValue("full"));
		this.belly = xml.getAttribute("belly") == null ? 0 : Integer.parseInt(xml.getAttributeValue("belly"));
	}

	public ItemFood(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, sprite, isStackable);
		this.food = food;
		this.bellyIfFull = bellyIfFull;
		this.belly = belly;
	}

	public Element toXML()
	{
		Element root = super.toXML();
		if (this.food != 0) root.setAttribute("food", Integer.toString(this.food));
		if (this.bellyIfFull != 0) root.setAttribute("full", Integer.toString(this.bellyIfFull));
		if (this.belly != 0) root.setAttribute("belly", Integer.toString(this.belly));
		return root;
	}

}

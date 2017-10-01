package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.Message;
import com.darkxell.common.util.XMLUtils;

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
		this.food = XMLUtils.getAttribute(xml, "food", 0);
		this.bellyIfFull = XMLUtils.getAttribute(xml, "full", 0);
		this.belly = XMLUtils.getAttribute(xml, "belly", 0);
	}

	public ItemFood(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, sprite, isStackable);
		this.food = food;
		this.bellyIfFull = bellyIfFull;
		this.belly = belly;
	}

	public ItemCategory category()
	{
		return ItemCategory.FOOD;
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.eat");
	}

	public Element toXML()
	{
		Element root = super.toXML();
		XMLUtils.setAttribute(root, "food", this.food, 0);
		XMLUtils.setAttribute(root, "full", this.bellyIfFull, 0);
		XMLUtils.setAttribute(root, "belly", this.belly, 0);
		return root;
	}

}

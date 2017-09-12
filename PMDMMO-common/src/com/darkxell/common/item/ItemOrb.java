package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.Message;

/** An Item that has different effects when used. */
public class ItemOrb extends Item
{

	public ItemOrb(Element xml)
	{
		super(xml);
	}

	public ItemOrb(int id, int price, int sell, int sprite, boolean isStackable)
	{
		super(id, price, sell, sprite, isStackable);
	}

	public ItemCategory category()
	{
		return ItemCategory.ORBS;
	}

	@Override
	public Message name()
	{
		return super.name().addPrefix("<orb>");
	}

}

package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that restores belly when eaten, and inflicts negative status effects. */
public class ItemFoodGrimy extends ItemFood
{

	public ItemFoodGrimy(Element xml)
	{
		super(xml);
	}

	public ItemFoodGrimy(int id, int price, int sell, int sprite, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, sprite, food, bellyIfFull, belly);
	}

}

package com.darkxell.common.item;

import org.jdom2.Element;

/** An Item that Increases the power of selected move by 1, and rarely 3. */
public class ItemGinseng extends ItemFood
{

	public ItemGinseng(Element xml)
	{
		super(xml);
	}

	public ItemGinseng(int id, int price, int sell, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, food, bellyIfFull, belly);
	}

}

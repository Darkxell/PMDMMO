package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

/** An Item that has an effect when hold. */
public class ItemEquipable extends Item
{

	public ItemEquipable(Element xml)
	{
		super(xml);
	}

	public ItemEquipable(int id, int price, int sell, int sprite, boolean isStackable)
	{
		super(id, price, sell, sprite, isStackable);
	}

	public ItemCategory category()
	{
		return ItemCategory.EQUIPABLE;
	}

	@Override
	public ArrayList<ItemAction> getLegalActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = super.getLegalActions(inDungeon);
		actions.remove(ItemAction.USE);
		return actions;
	}

}

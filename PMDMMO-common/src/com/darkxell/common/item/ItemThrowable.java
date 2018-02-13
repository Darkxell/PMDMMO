package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.util.language.Message;

public class ItemThrowable extends Item
{
	public static enum ThrowableTrajectory
	{
		Arc,
		Straight
	}

	/** The damage this throwable Item deals. */
	public final int damage;
	/** The type of the trajectory. */
	public final ThrowableTrajectory trajectory;

	public ItemThrowable(Element xml)
	{
		super(xml);
		this.damage = Integer.parseInt(xml.getAttributeValue("damage"));
		this.trajectory = ThrowableTrajectory.valueOf(xml.getAttributeValue("trajectory"));
	}

	public ItemThrowable(int id, int price, int sell, int sprite, boolean isStackable, int damage, ThrowableTrajectory trajectory)
	{
		super(id, price, sell, sprite, isStackable);
		this.damage = damage;
		this.trajectory = trajectory;
	}

	public ItemCategory category()
	{
		return ItemCategory.THROWABLE;
	}

	@Override
	public ArrayList<ItemAction> getLegalActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = super.getLegalActions(inDungeon);
		actions.remove(ItemAction.THROW);
		return actions;
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.throw");
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("trajectory", this.trajectory.name());
		root.setAttribute("damage", Integer.toString(this.damage));
		return root;
	}

}

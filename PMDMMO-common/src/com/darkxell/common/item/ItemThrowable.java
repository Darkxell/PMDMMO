package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.util.Message;

public class ItemThrowable extends Item
{
	/** Trajectory types.<br />
	 * <ul>
	 * <li>STRAIGHT (spikes) = 0</li>
	 * <li>ARC (gravelerock) = 1</li>
	 * </ul> */
	public static final byte STRAIGHT = 0, ARC = 1;

	/** The damage this throwable Item deals. */
	public final int damage;
	/** The type of the trajectory. See {@link ItemThrowable#STRAIGHT}. */
	public final byte trajectory;

	public ItemThrowable(Element xml)
	{
		super(xml);
		this.damage = Integer.parseInt(xml.getAttributeValue("damage"));
		this.trajectory = Byte.parseByte(xml.getAttributeValue("trajectory"));
	}

	public ItemThrowable(int id, int price, int sell, int sprite, boolean isStackable, int damage, byte trajectory)
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
		root.setAttribute("trajectory", Integer.toString(this.trajectory));
		root.setAttribute("damage", Integer.toString(this.damage));
		return root;
	}

}

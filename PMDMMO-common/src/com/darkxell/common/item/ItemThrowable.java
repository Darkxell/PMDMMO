package com.darkxell.common.item;

import org.jdom2.Element;

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

	public ItemThrowable(int id, int price, int sell, int damage, byte trajectory)
	{
		super(id, price, sell);
		this.damage = damage;
		this.trajectory = trajectory;
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

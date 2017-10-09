package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

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
		this.hp = XMLUtils.getAttribute(xml, "hp", 0);
		this.hpFull = XMLUtils.getAttribute(xml, "hp-full", 0);
	}

	public ItemHeal(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly, int hp, int hpFull)
	{
		super(id, price, sell, sprite, isStackable, food, bellyIfFull, belly);
		this.hp = hp;
		this.hpFull = hpFull;
	}

	public ItemCategory category()
	{
		return ItemCategory.BERRIES;
	}

	@Override
	protected String getUseID()
	{
		return "item.eaten";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		XMLUtils.setAttribute(root, "hp", this.hp, 0);
		XMLUtils.setAttribute(root, "hp-full", this.hpFull, 0);
		return root;
	}
	
	@Override
	public boolean usedOnTeamMember()
	{
		return true;
	}

}

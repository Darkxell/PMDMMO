package com.darkxell.common.item;

public class ItemStack
{

	/** The ID of the Item. */
	public final int id;
	/** The number of Items in this Stack. Almost always 1 except for Poké, Gravelerock and similar items. */
	private int quantity;

	public ItemStack(int id)
	{
		this.id = id;
		this.quantity = 1;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public Item item()
	{
		return ItemRegistry.find(this.id);
	}

	public ItemStack setQuantity(int quantity)
	{
		this.quantity = quantity;
		return this;
	}

}

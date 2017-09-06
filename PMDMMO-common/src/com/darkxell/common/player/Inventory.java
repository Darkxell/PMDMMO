package com.darkxell.common.player;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;

public class Inventory
{

	public static final int MAX_SIZE = 20;

	private ArrayList<ItemStack> items;
	private int maxSize;

	public Inventory(int maxSize)
	{
		this.maxSize = maxSize;
		this.items = new ArrayList<ItemStack>();
	}

	/** @return True if the input Item was successfully added. */
	public boolean add(ItemStack item)
	{
		if (!canAccept(item)) return false;
		if (item.item().isStackable) for (ItemStack stack : this.items)
			if (stack.id == item.id)
			{
				stack.setQuantity(stack.getQuantity() + item.getQuantity());
				return true;
			}

		this.items.add(item);
		return true;
	}

	/** @return True if the input Item can be added. */
	public boolean canAccept(ItemStack item)
	{
		if (item.item().isStackable) for (ItemStack stack : this.items)
			if (stack.id == item.id) return true;

		return !this.isFull();
	}

	/** Removes all Items with quantity equal to zero. */
	private void clean()
	{
		this.items.removeIf(new Predicate<ItemStack>()
		{
			@Override
			public boolean test(ItemStack item)
			{
				return item.getQuantity() == 0;
			}
		});
	}

	/** @return The Item at the input slot. */
	public ItemStack get(int slot)
	{
		if (slot < 0 || slot >= this.maxSize) return null;
		return this.items.get(slot);
	}

	public boolean isEmpty()
	{
		return this.items.isEmpty();
	}

	public boolean isFull()
	{
		return this.items.size() == this.maxSize;
	}

	public int itemCount()
	{
		return this.items.size();
	}

	/** @return The list of Items in this Inventory. */
	public ItemStack[] list()
	{
		return this.items.toArray(new ItemStack[this.items.size()]);
	}

	public int maxSize()
	{
		return this.maxSize;
	}

	public ItemStack remove(Item item, int quantity)
	{
		ItemStack toreturn = new ItemStack(item.id);
		toreturn.setQuantity(0);

		for (ItemStack stack : this.items)
		{
			if (stack.id == item.id)
			{
				int remove = Math.min(quantity, stack.getQuantity());
				stack.setQuantity(stack.getQuantity() - remove);
				quantity -= remove;
				toreturn.setQuantity(toreturn.getQuantity() + remove);
				if (quantity == 0) break;
			}
		}

		this.clean();

		if (toreturn.getQuantity() == 0) return null;
		return toreturn;
	}

}

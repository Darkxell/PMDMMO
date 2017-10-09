package com.darkxell.common.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.language.Message;

public class Inventory implements ItemContainer
{

	public static final int MAX_SIZE = 20;

	private ArrayList<ItemStack> items;
	private int maxSize;

	public Inventory(int maxSize)
	{
		this.maxSize = maxSize;
		this.items = new ArrayList<ItemStack>();
	}

	@Override
	public void addItem(ItemStack item)
	{
		if (item.item().isStackable) for (ItemStack stack : this.items)
			if (stack.id == item.id)
			{
				stack.setQuantity(stack.getQuantity() + item.getQuantity());
				return;
			}

		this.items.add(item);
	}

	@Override
	public int canAccept(ItemStack item)
	{
		if (this.isFull()) return -1;

		if (item.item().isStackable) for (ItemStack stack : this.items)
			if (stack.id == item.id) return this.items.indexOf(stack);

		return this.size();
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

	@Override
	public Message containerName()
	{
		return new Message("inventory.toolbox");
	}

	@Override
	public void deleteItem(int index)
	{
		this.remove(index);
	}

	@Override
	public ItemStack getItem(int index)
	{
		if (index < 0 || index >= this.maxSize) return null;
		return this.items.get(index);
	}

	public boolean isEmpty()
	{
		return this.items.isEmpty();
	}

	public boolean isFull()
	{
		return this.items.size() == this.maxSize;
	}

	@Override
	public ArrayList<ItemAction> legalItemActions()
	{
		ArrayList<ItemAction> actions = new ArrayList<ItemAction>();
		actions.add(ItemAction.GIVE);
		actions.add(ItemAction.SWITCH);
		actions.add(ItemAction.PLACE);
		return actions;
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

	/** Removes the Item in the input slot and returns it. Returns null if index is out of bounds. */
	public ItemStack remove(int slot)
	{
		if (slot < 0 || slot >= this.maxSize) return null;
		ItemStack i = this.items.get(slot);
		this.items.remove(slot);
		return i;
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

	@Override
	public void setItem(int index, ItemStack item)
	{
		this.items.set(index, item);
	}

	@Override
	public int size()
	{
		return this.items.size();
	}

	public void sort()
	{
		this.items.sort(Comparator.naturalOrder());
	}

}

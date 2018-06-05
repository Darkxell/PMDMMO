package com.darkxell.common.player;

import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.language.Message;

public class Inventory implements ItemContainer
{

	public static final int MAX_SIZE = 20;

	private DBInventory data;

	private ArrayList<ItemStack> items;

	public Inventory()
	{
		this(MAX_SIZE);
	}

	public Inventory(DBInventory data)
	{
		this.setData(data);
	}

	public Inventory(int maxSize)
	{
		this(new DBInventory(0, maxSize, new ArrayList<>()));
	}

	@Override
	public void addItem(ItemStack item)
	{
		ArrayList<ItemStack> items = this.items();

		if (item.item().isStackable) for (ItemStack stack : items)
			if (stack.item().id == item.item().id)
			{
				stack.setQuantity(stack.quantity() + item.quantity());
				return;
			}

		this.data.content.add(new DatabaseIdentifier(item.getData().id)); // Assuming Item IDs are assigned correctly.
		items.add(item);
	}

	@Override
	public int canAccept(ItemStack item)
	{
		if (this.isFull()) return -1;

		if (item.item().isStackable)
		{
			ArrayList<ItemStack> items = this.items();
			for (ItemStack stack : items)
				if (stack.item().id == item.item().id) return items.indexOf(stack);
		}

		return this.size();
	}

	/** Removes all Items with quantity equal to zero. */
	private void clean()
	{
		ArrayList<ItemStack> items = this.items();
		for (int i = 0; i < items.size(); ++i)
			if (items.get(i).quantity() <= 0)
			{
				items.remove(i);
				this.data.content.remove(i);
				--i;
			}
	}

	@Override
	public long containerID()
	{
		return this.getData().id;
	}

	@Override
	public Message containerName()
	{
		return new Message("inventory.toolbox");
	}

	@Override
	public ItemContainerType containerType()
	{
		return ItemContainerType.INVENTORY;
	}

	@Override
	public void deleteItem(int index)
	{
		this.remove(index);
	}

	public void empty()
	{
		this.data.content.clear();
		this.items.clear();
	}

	public DBInventory getData()
	{
		return this.data;
	}

	@Override
	public ItemStack getItem(int index)
	{
		if (index < 0 || index >= this.maxSize()) return null;
		return this.items().get(index);
	}

	public boolean isEmpty()
	{
		return this.items().isEmpty();
	}

	public boolean isFull()
	{
		return this.items().size() == this.maxSize();
	}

	public ArrayList<ItemStack> items()
	{
		return this.items;
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
		return this.data.maxsize;
	}

	/** Removes the Item in the input slot and returns it. Returns null if index is out of bounds. */
	public ItemStack remove(int slot)
	{
		if (slot < 0 || slot >= this.maxSize()) return null;
		ItemStack i = this.items.get(slot);
		this.data.content.remove(slot);
		this.items.remove(slot);
		return i;
	}

	public ItemStack remove(Item item, long quantity)
	{
		ItemStack toreturn = new ItemStack(item.id);
		toreturn.setQuantity(0);

		for (ItemStack stack : this.items)
		{
			if (stack.item().id == item.id)
			{
				long remove = Math.min(quantity, stack.quantity());
				stack.setQuantity(stack.quantity() - remove);
				quantity -= remove;
				toreturn.setQuantity(toreturn.quantity() + remove);
				if (quantity == 0) break;
			}
		}

		this.clean();

		if (toreturn.quantity() == 0) return null;
		return toreturn;
	}

	public void setData(DBInventory data)
	{
		this.data = data;
		this.items = new ArrayList<>();
	}

	@Override
	public void setItem(int index, ItemStack item)
	{
		this.data.content.set(index, new DatabaseIdentifier(item.getData().id)); // Assuming Item IDs are assigned correctly
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
		this.data.content.clear();
		for (ItemStack i : this.items)
			this.data.content.add(new DatabaseIdentifier(i.getData().id)); // Assuming Item IDs are assigned correctly
	}

}

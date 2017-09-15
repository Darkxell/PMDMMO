package com.darkxell.common.player;

import java.util.ArrayList;

import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.Message;

/** Represents an object that can store Items. */
public interface ItemContainer
{

	/** Adds an Item to this Container. */
	public void addItem(ItemStack item);

	/** @return -1 if this Container can't accept the input Item. If it can, returns the index at which it will accept it. */
	public int canAccept(ItemStack item);

	public Message containerName();

	/** Deletes the Item at the input index. */
	public void deleteItem(int index);

	/** @return The Item at the input index. */
	public ItemStack getItem(int index);

	/** @return The list of movement Item Actions legal for this Container. */
	public ArrayList<ItemAction> legalItemActions();

	/** Sets the Item at the input index. */
	public void setItem(int index, ItemStack item);

	/** @return The current number of Items in this Container. */
	public int size();

}

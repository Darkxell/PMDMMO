package com.darkxell.common.player;

import java.util.ArrayList;

import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.language.Message;

/** Represents an object that can store Items. */
public interface ItemContainer
{

	public static enum ItemContainerType
	{
		DUNGEON_POKEMON,
		INVENTORY,
		POKEMON,
		TILE;
	}

	/** Adds an Item to this Container. */
	public void addItem(ItemStack item);

	/** @return -1 if this Container can't accept the input Item. If it can, returns the index at which it will accept it. */
	public int canAccept(ItemStack item);

	/** @return The ID of this Container. Required for Event communication. */
	public long containerID();

	public Message containerName();

	/** @return The type of the Container. Required for Event communication. */
	public ItemContainerType containerType();

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

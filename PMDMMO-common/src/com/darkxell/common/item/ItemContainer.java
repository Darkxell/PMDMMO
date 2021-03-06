package com.darkxell.common.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.util.language.Message;

/** Represents an object that can store Items. */
public interface ItemContainer {

    public enum ItemContainerType {
        DUNGEON_POKEMON,
        INVENTORY,
        POKEMON,
        TILE
    }

    /** Adds an Item to this Container. */
    public void addItem(ItemStack item);

    /**
     * @return -1 if this Container can't accept the input Item. If it can, returns the index at which it will accept
     *         it.
     */
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

    /**
     * @param  inDungeon - True if actions for dungeons can be included.
     * @return           The list of movement Item Actions legal for this Container.
     */
    public ArrayList<ItemAction> legalItemActions(boolean inDungeon);

    /** @return The Tile this container is currently located at, if in a Dungeon. null else. */
    public Tile locationOnFloor();

    /** Sets the Item at the input index. */
    public void setItem(int index, ItemStack item);

    /** @return The current number of Items in this Container. */
    public int size();

}

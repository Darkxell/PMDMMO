package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;

public class ItemLandedEvent extends DungeonEvent
{

	private Tile destination;
	public final Item item;
	private ItemStack placedItem;
	public final Tile tile;

	public ItemLandedEvent(Floor floor, Item item, Tile tile)
	{
		super(floor);
		this.item = item;
		this.tile = tile;
	}

	public Tile destination()
	{
		return this.destination;
	}

	@Override
	public String loggerMessage()
	{
		return this.item + " lands on " + this.tile;
	}

	public ItemStack placedItem()
	{
		return this.placedItem;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.destination = this.tile;
		this.placedItem = new ItemStack(this.item.id);
		this.destination.setItem(this.placedItem);
		this.floor.dungeon.communication.itemIDs.register(this.placedItem, null);
		return super.processServer();
	}

}

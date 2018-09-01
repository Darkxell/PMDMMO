package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;

public class ItemCreatedEvent extends DungeonEvent
{

	public final ItemContainer container;
	public final ItemStack item;

	public ItemCreatedEvent(Floor floor, ItemStack item, ItemContainer container)
	{
		super(floor);
		this.item = item;
		this.container = container;
	}

	@Override
	public String loggerMessage()
	{
		return "Created item: " + this.item.name();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		// TODO Put created Item somewhere else if there is no room in Container
		if (this.container.canAccept(this.item) != -1) this.container.addItem(this.item);
		return super.processServer();
	}

}

package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

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
		this.floor.dungeon.communication.itemIDs.register(this.item, this.container instanceof Pokemon ? ((Pokemon) this.container)
				: this.container instanceof DungeonPokemon ? ((DungeonPokemon) this.container).usedPokemon : null);
		if (this.container.canAccept(this.item) != -1) this.container.addItem(this.item);
		return super.processServer();
	}

}

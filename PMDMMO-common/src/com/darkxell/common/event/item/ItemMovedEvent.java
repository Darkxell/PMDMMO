package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class ItemMovedEvent extends DungeonEvent
{

	public final DungeonPokemon mover;
	public final ItemContainer source, destination;
	public final int sourceIndex, destinationIndex;

	public ItemMovedEvent(Floor floor, ItemAction action, DungeonPokemon mover, ItemContainer source, int sourceIndex, ItemContainer destination,
			int destinationIndex)
	{
		super(floor);
		this.mover = mover;
		this.source = source;
		this.sourceIndex = sourceIndex;
		this.destination = destination;
		this.destinationIndex = destinationIndex;

		String message = null;
		if (action == ItemAction.GIVE) message = "inventory.give";
		else if (action == ItemAction.GET)
		{
			if (destination instanceof Inventory) message = "ground.inventory";
			else message = "ground.pickup";
		} else if (action == ItemAction.PLACE) message = "ground.place";
		else if (action == ItemAction.TAKE) message = "inventory.taken";
		this.messages.add(new Message(message).addReplacement("<pokemon>", mover.pokemon.getNickname()).addReplacement("<item>",
				this.source.getItem(this.sourceIndex).name()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		ItemStack i = this.source.getItem(this.sourceIndex);
		this.source.deleteItem(this.sourceIndex);
		if (this.destinationIndex >= this.destination.size()) this.destination.addItem(i);
		else this.destination.setItem(this.destinationIndex, i);
		return super.processServer();
	}

}

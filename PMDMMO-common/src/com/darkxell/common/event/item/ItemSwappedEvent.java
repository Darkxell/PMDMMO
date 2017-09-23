package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class ItemSwappedEvent extends DungeonEvent
{

	public final DungeonPokemon mover;
	public final ItemContainer source, destination;
	public final int sourceIndex, destinationIndex;

	public ItemSwappedEvent(ItemAction action, DungeonPokemon mover, ItemContainer source, int sourceIndex, ItemContainer destination, int destinationIndex)
	{
		this.mover = mover;
		this.source = source;
		this.sourceIndex = sourceIndex;
		this.destination = destination;
		this.destinationIndex = destinationIndex;

		String message = null;
		if (action == ItemAction.GIVE) message = "inventory.swap";
		if (action == ItemAction.SWITCH || action == ItemAction.SWAP) message = "ground.swap";
		this.messages.add(new Message(message).addReplacement("<pokemon>", mover.pokemon.getNickname())
				.addReplacement("<item-given>", this.source.getItem(this.sourceIndex).name())
				.addReplacement("<item-gotten>", this.destination.getItem(this.destinationIndex).name()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		ItemStack i = this.source.getItem(this.sourceIndex);
		this.source.setItem(this.sourceIndex, this.destination.getItem(this.destinationIndex));
		this.destination.setItem(this.destinationIndex, i);
		return super.processServer();
	}
}

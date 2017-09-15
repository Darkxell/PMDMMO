package com.darkxell.common.event.item;

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

		if (action == ItemAction.SWITCH || action == ItemAction.SWAP) this.messages.add(new Message("ground.swap")
				.addReplacement("<pokemon>", mover.pokemon.getNickname()).addReplacement("<item-placed>", this.source.getItem(this.sourceIndex).name())
				.addReplacement("<item-gotten>", this.destination.getItem(this.destinationIndex).name()));
	}

	@Override
	public DungeonEvent[] processServer()
	{
		ItemStack i = this.source.getItem(this.sourceIndex);
		this.source.setItem(this.sourceIndex, this.destination.getItem(this.destinationIndex));
		this.destination.setItem(this.destinationIndex, i);
		return super.processServer();
	}
}

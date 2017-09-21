package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class ItemMovedEvent extends DungeonEvent
{

	public final DungeonPokemon mover;
	public final ItemContainer source, destination;
	public final int sourceIndex, destinationIndex;

	public ItemMovedEvent(ItemAction action, DungeonPokemon mover, ItemContainer source, int sourceIndex, ItemContainer destination, int destinationIndex)
	{
		this.mover = mover;
		this.source = source;
		this.sourceIndex = sourceIndex;
		this.destination = destination;
		this.destinationIndex = destinationIndex;

		if (action == ItemAction.GET) this.messages.add(new Message("ground.inventory").addReplacement("<pokemon>", mover.pokemon.getNickname())
				.addReplacement("<item>", this.source.getItem(this.sourceIndex).name()));
		if (action == ItemAction.PLACE) this.messages.add(new Message("ground.place").addReplacement("<pokemon>", mover.pokemon.getNickname()).addReplacement(
				"<item>", this.source.getItem(this.sourceIndex).name()));
		if (action == ItemAction.TAKE) this.messages.add(new Message("inventory.taken").addReplacement("<pokemon>", mover.pokemon.getNickname())
				.addReplacement("<item>", this.source.getItem(this.sourceIndex).name()));
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

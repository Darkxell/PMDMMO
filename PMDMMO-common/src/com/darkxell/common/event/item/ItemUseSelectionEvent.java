package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Describes the events occurring before using an Item. */
public class ItemUseSelectionEvent extends DungeonEvent
{

	/** The Floor the user is on. */
	public final Floor floor;
	/** The Item that was used. */
	public final Item item;
	/** The Container the Item was from. */
	public final ItemContainer source;
	/** The index of the Item in the source Container. */
	public final int sourceIndex;
	/** The Pokémon that the Item was used on. null if there was no target. */
	public final DungeonPokemon target;
	/** The Pokémon that used the Item. */
	public final DungeonPokemon user;

	public ItemUseSelectionEvent(Item item, DungeonPokemon user, DungeonPokemon target, ItemContainer source, int sourceIndex, Floor floor)
	{
		this.item = item;
		this.user = user;
		this.target = target;
		this.floor = floor;
		this.source = source;
		this.sourceIndex = sourceIndex;

		this.messages.add(this.item.getUseMessage(this.user));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		ItemStack stack = this.source.getItem(this.sourceIndex);
		stack.setQuantity(stack.getQuantity() - 1);
		if (stack.getQuantity() <= 0) this.source.deleteItem(this.sourceIndex);

		ArrayList<DungeonEvent> events = super.processServer();
		events.add(new ItemUseEvent(this.item, user, this.target, this.floor));
		return events;
	}

}

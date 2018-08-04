package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class BellySizeChangedEvent extends DungeonEvent
{
	public final DungeonPokemon pokemon;
	/** How much the Pokemon's belly size was changed. */
	public final int quantity;

	public BellySizeChangedEvent(Floor floor, DungeonPokemon pokemon, int quantity)
	{
		super(floor);
		this.pokemon = pokemon;
		this.quantity = quantity;
	}

	@Override
	public String loggerMessage()
	{
		return this.messages.get(0).toString();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages
				.add(new Message(this.quantity > 0 ? "belly.size.increased" : "belly.size.reduced").addReplacement("<pokemon>", this.pokemon.getNickname()));

		this.pokemon.increaseBellySize(this.quantity);
		return super.processServer();
	}

}

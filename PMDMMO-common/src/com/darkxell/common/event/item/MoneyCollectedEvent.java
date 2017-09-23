package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class MoneyCollectedEvent extends DungeonEvent
{

	public final ItemStack moneyItem;
	public final DungeonPokemon pokemon;
	public final Tile tile;

	public MoneyCollectedEvent(DungeonPokemon pokemon, Tile tile, ItemStack moneyItem)
	{
		this.pokemon = pokemon;
		this.tile = tile;
		this.moneyItem = moneyItem;

		this.messages.add(new Message("ground.pickup").addReplacement("<pokemon>", this.pokemon.pokemon.getNickname()).addReplacement("<item>",
				this.moneyItem.name()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.tile.setItem(null);
		this.pokemon.pokemon.player.money += this.moneyItem.getQuantity();
		return super.processServer();
	}

}

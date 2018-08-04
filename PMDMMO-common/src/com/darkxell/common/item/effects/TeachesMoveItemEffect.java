package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that teaches a move to a Pokemon when used, then turns into a Used TM. */
public class TeachesMoveItemEffect extends TeachesMoveRenewableItemEffect
{

	public TeachesMoveItemEffect(int id, int moveID)
	{
		super(id, moveID);
	}

	@Override
	public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		if (pokemon.player() != null)
		{
			if (pokemon.player().inventory().isFull()) pokemon.tile().setItem(new ItemStack(-1 * item.id));
			else pokemon.player().inventory().addItem(new ItemStack(-1 * item.id));
		}
	}
}

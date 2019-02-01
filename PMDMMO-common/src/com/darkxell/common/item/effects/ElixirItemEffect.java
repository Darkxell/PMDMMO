package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that restores PP when eaten. */
public class ElixirItemEffect extends DrinkItemEffect
{

	public final int pp;

	public ElixirItemEffect(int id, int food, int bellyIfFull, int belly, int pp)
	{
		super(id, food, bellyIfFull, belly);
		this.pp = pp;
	}

	@Override
	public boolean isUsedOnTeamMember()
	{
		return true;
	}

	@Override
	public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		super.use(floor, item, pokemon, target, events);
		events.add(new PPChangedEvent(floor, target, this.pp, PPChangedEvent.CHANGE_ALL_MOVES));
	}

}

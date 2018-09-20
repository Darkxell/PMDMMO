package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that restores belly when eaten, and inflicts negative status effects. */
public class HealFoodItemEffect extends FoodItemEffect
{

	/** The amount of health this Item restores when eaten. */
	public final int hp;
	/** The amount of health this Item adds to the maximum health when eaten with full health. */
	public final int hpFull;

	public HealFoodItemEffect(int id, int food, int bellyIfFull, int belly, int hp, int hpFull)
	{
		super(id, food, bellyIfFull, belly);
		this.hp = hp;
		this.hpFull = hpFull;
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
		if (pokemon.getBelly() < pokemon.getBellySize()) events.add(new HealthRestoredEvent(floor, target, this.hp));
	}

}

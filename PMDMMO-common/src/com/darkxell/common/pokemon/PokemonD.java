package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditionInstance;

/** Represents a Pok�mon in a Dungeon. */
public class PokemonD
{
	public static final int DEFAULT_BELLY_SIZE = 100;

	/** This Pok�mon's current belly points. */
	private int belly;
	/** This Pok�mon's belly size. */
	private int bellySize;
	/** This Pok�mon's current Hit Points. */
	private int hp;
	/** This Pok�mon's data. */
	public final Pokemon pokemon;
	/** This Pok�mon's stats for the current dungeon. */
	public final DungeonStats stats;
	/** This Pok�mon's active Status Conditions. */
	private final ArrayList<StatusConditionInstance> statusConditions;

	public PokemonD(Pokemon pokemon)
	{
		this.pokemon = pokemon;
		this.stats = new DungeonStats(this.pokemon.stats);
		this.belly = this.bellySize = DEFAULT_BELLY_SIZE;
		this.hp = this.stats.getHealth();
		this.statusConditions = new ArrayList<StatusConditionInstance>();
	}

	public int getBelly()
	{
		return this.belly;
	}

	public int getBellySize()
	{
		return this.bellySize;
	}

	public int getHp()
	{
		return this.hp;
	}

	/** @return True if this Pok�mon is affected by the input Status Condition. */
	public boolean hasStatusCondition(StatusCondition condition)
	{
		for (StatusConditionInstance c : this.statusConditions)
			if (c.condition == condition) return true;
		return false;
	}

}

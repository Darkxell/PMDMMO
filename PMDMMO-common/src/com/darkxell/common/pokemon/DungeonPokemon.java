package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditionInstance;
import com.darkxell.common.util.GameUtil;

/** Represents a Pokémon in a Dungeon. */
public class DungeonPokemon
{
	public static final int DEFAULT_BELLY_SIZE = 100;
	public static final byte REGULAR_ATTACKS = 0, MOVES = 1, LINKED_MOVE = 2;

	/** The attacks this Pokémon has received. Use in experience calculation. */
	private byte attacksReceived = REGULAR_ATTACKS;
	/** This Pokémon's current belly points. */
	private int belly;
	/** This Pokémon's belly size. */
	private int bellySize;
	/** The direction this Pokémon is facing. */
	private short facing = GameUtil.SOUTH;
	/** This Pokémon's current Hit Points. */
	private int hp;
	/** This Pokémon's data. */
	public final Pokemon pokemon;
	/** True if this Pokémon's state changed (direction, state...). Used for rendering. */
	public boolean stateChanged;
	/** This Pokémon's stats for the current dungeon. */
	public final DungeonStats stats;
	/** This Pokémon's active Status Conditions. */
	private final ArrayList<StatusConditionInstance> statusConditions;
	/** The tile this Pokémon is standing on. */
	public Tile tile;

	public DungeonPokemon(Pokemon pokemon)
	{
		this.pokemon = pokemon;
		this.stats = new DungeonStats(this.pokemon.getStats());
		this.belly = this.bellySize = DEFAULT_BELLY_SIZE;
		this.hp = this.stats.getHealth();
		this.statusConditions = new ArrayList<StatusConditionInstance>();
	}

	/** @return The amount of experience gained when defeating this Pokémon. */
	public int experienceGained()
	{
		int base = this.pokemon.species.baseXP;
		base += Math.floor(base * (this.pokemon.getLevel() - 1) / 10);
		if (this.attacksReceived == REGULAR_ATTACKS) base = (int) (base * 0.5);
		else if (this.attacksReceived == LINKED_MOVE) base = (int) (base * 1.5);
		return base;
	}

	/** @return The direction this Pokémon is facing. */
	public short facing()
	{
		return this.facing;
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

	/** @return True if this Pokémon is affected by the input Status Condition. */
	public boolean hasStatusCondition(StatusCondition condition)
	{
		for (StatusConditionInstance c : this.statusConditions)
			if (c.condition == condition) return true;
		return false;
	}

	/** Changes the direction this Pokémon is facing. */
	public void setFacing(short direction)
	{
		this.facing = direction;
		this.stateChanged = true;
	}

	public void setHP(int hp)
	{
		this.hp = hp;
		if (this.hp < 0) this.hp = 0;
		if (this.hp > this.pokemon.getStats().health) this.hp = this.pokemon.getStats().health;
	}

	/** Called when this Pokémon tries to move in the input direction. */
	public boolean tryMoveTo(short direction)
	{
		boolean success = false;
		if (this.tile != null)
		{
			Tile t = this.tile.adjacentTile(direction);
			if (t.canMoveTo(this, direction)) success = true;
		}
		this.setFacing(direction);
		return success;
	}

}

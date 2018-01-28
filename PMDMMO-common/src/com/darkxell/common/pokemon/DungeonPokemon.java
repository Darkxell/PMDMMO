package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditionInstance;
import com.darkxell.common.util.Direction;

/** Represents a Pokémon in a Dungeon. */
public class DungeonPokemon
{
	public static final int DEFAULT_BELLY_SIZE = 100;
	public static final byte REGULAR_ATTACKS = 0, MOVES = 1, LINKED_MOVES = 2;

	/** The attacks this Pokémon has received. Use in experience calculation. */
	private byte attacksReceived = REGULAR_ATTACKS;
	/** This Pokémon's current belly points. */
	private double belly;
	/** This Pokémon's belly size. */
	private int bellySize;
	/** The direction this Pokémon is facing. */
	private Direction facing = Direction.SOUTH;
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
	private Tile tile;

	public DungeonPokemon(Pokemon pokemon)
	{
		this.pokemon = pokemon;
		this.stats = new DungeonStats(this);
		this.belly = this.bellySize = DEFAULT_BELLY_SIZE;
		this.hp = this.stats.getHealth();
		this.statusConditions = new ArrayList<StatusConditionInstance>();
		pokemon.dungeonPokemon = this;
	}

	/** Clears references to this Dungeon Pokémon in the Pokémon object. */
	public void dispose()
	{
		this.pokemon.dungeonPokemon = null;
	}

	/** @return The multiplier to apply to base energy values for the team leader's actions. Used to determine how much belly is lost for that action. */
	public double energyMultiplier()
	{
		return 1;
	}

	/** @return The amount of experience gained when defeating this Pokémon. */
	public int experienceGained()
	{
		int base = this.pokemon.species.baseXP;
		base += Math.floor(base * (this.pokemon.getLevel() - 1) / 10) + base;
		if (this.attacksReceived == REGULAR_ATTACKS) base = (int) (base * 0.5);
		else if (this.attacksReceived == LINKED_MOVES) base = (int) (base * 1.5);
		return base;
	}

	/** @return The direction this Pokémon is facing. */
	public Direction facing()
	{
		return this.facing;
	}

	public double getBelly()
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

	public int getMaxHP()
	{
		return this.pokemon.getStats().getHealth();
	}

	/** @return True if this Pokémon is affected by the input Status Condition. */
	public boolean hasStatusCondition(StatusCondition condition)
	{
		for (StatusConditionInstance c : this.statusConditions)
			if (c.condition == condition) return true;
		return false;
	}

	public void increaseBelly(double quantity)
	{
		this.belly += quantity;
		this.belly = this.belly < 0 ? 0 : this.belly > this.getBellySize() ? this.getBellySize() : this.belly;
	}

	public void increaseBellySize(int quantity)
	{
		this.bellySize += quantity;
		this.bellySize = this.bellySize < 0 ? 0 : this.bellySize > 200 ? 200 : this.bellySize;
		this.increaseBelly(quantity);
	}

	public void inflictStatusCondition(StatusConditionInstance condition)
	{
		this.statusConditions.add(condition);
	}

	public boolean isBellyFull()
	{
		return this.getBelly() == this.getBellySize();
	}

	public boolean isFainted()
	{
		return this.getHp() <= 0;
	}

	public boolean isFamished()
	{
		return this.getBelly() == 0;
	}

	public boolean isTeamLeader()
	{
		return this.pokemon.player != null && this.pokemon.player.getDungeonLeader() == this;
	}

	/** Called when this Pokémon enters a new Floor or when it spawns. */
	public ArrayList<DungeonEvent> onFloorStart(Floor floor)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		this.statusConditions.clear();
		return events;
	}

	/** Called at the beginning of each turn. */
	public ArrayList<DungeonEvent> onTurnStart(Floor floor)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		for (StatusConditionInstance condition : this.statusConditions)
			events.addAll(condition.tick(floor));
		return events;
	}

	public void receiveMove(byte attackType)
	{
		if (attackType > this.attacksReceived) this.attacksReceived = attackType;
	}

	public void removeStatusCondition(StatusCondition condition)
	{
		this.statusConditions.removeIf(new Predicate<StatusConditionInstance>() {
			@Override
			public boolean test(StatusConditionInstance t)
			{
				return t.condition == condition;
			}
		});
	}

	public void removeStatusCondition(StatusConditionInstance condition)
	{
		this.statusConditions.remove(condition);
	}

	/** Changes the direction this Pokémon is facing. */
	public void setFacing(Direction direction)
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

	public void setTile(Tile tile)
	{
		this.tile = tile;
	}

	public Tile tile()
	{
		return this.tile;
	}

	@Override
	public String toString()
	{
		return this.pokemon.getNickname().toString();
	}

	/** Called when this Pokémon tries to move in the input direction. */
	public boolean tryMoveTo(Direction direction, boolean allowSwitching)
	{
		boolean success = false;
		if (this.tile != null)
		{
			Tile t = this.tile.adjacentTile(direction);
			if (t.canMoveTo(this, direction, allowSwitching)) success = true;
		}
		this.setFacing(direction);
		return success;
	}

}

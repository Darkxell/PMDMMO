package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditionInstance;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

/** Represents a Pokémon in a Dungeon. */
public class DungeonPokemon implements ItemContainer
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
	/** The original Pokémon that entered the Dungeon. This object is necessary for Dungeons that modify the visiting Pokémon, such as Dungeons resetting the level to 1. */
	public final Pokemon originalPokemon;
	/** Variable used to compute HP regeneration. */
	private int regenCounter = 0;
	/** This Pokémon's stats for the current dungeon. */
	public final DungeonStats stats;
	/** This Pokémon's active Status Conditions. */
	private final ArrayList<StatusConditionInstance> statusConditions;
	/** The tile this Pokémon is standing on. */
	private Tile tile;
	/** The Pokémon to use in the current Dungeon. See {@link DungeonPokemon#originalPokemon}. */
	public final Pokemon usedPokemon;

	public DungeonPokemon(Pokemon pokemon)
	{
		this.usedPokemon = pokemon;
		this.originalPokemon = pokemon;
		this.stats = new DungeonStats(this);
		this.belly = this.bellySize = DEFAULT_BELLY_SIZE;
		this.hp = this.stats.getHealth();
		this.statusConditions = new ArrayList<StatusConditionInstance>();
		pokemon.dungeonPokemon = this;
	}

	public Ability ability()
	{
		return this.usedPokemon.ability();
	}

	@Override
	public void addItem(ItemStack item)
	{
		this.usedPokemon.addItem(item);
		if (this.isCopy()) this.originalPokemon.addItem(item);
	}

	@Override
	public int canAccept(ItemStack item)
	{
		return this.usedPokemon.canAccept(item);
	}

	/** @return True if this Pokémon can regenerate HP. */
	public boolean canRegen()
	{
		return !this.hasStatusCondition(StatusCondition.BADLY_POISONED) && !this.hasStatusCondition(StatusCondition.POISONED);
	}

	@Override
	public Message containerName()
	{
		return this.usedPokemon.containerName();
	}

	@Override
	public void deleteItem(int index)
	{
		this.originalPokemon.deleteItem(index);
	}

	/** Clears references to this Dungeon Pokémon in the Pokémon object. */
	public void dispose()
	{
		this.originalPokemon.dungeonPokemon = null;
	}

	/** @return The multiplier to apply to base energy values for the team leader's actions. Used to determine how much belly is lost for that action. */
	public double energyMultiplier()
	{
		return 1;
	}

	/** @return The amount of experience gained when defeating this Pokémon. */
	public int experienceGained()
	{
		int base = this.usedPokemon.species().baseXP;
		base += Math.floor(base * (this.originalPokemon.level() - 1) / 10) + base;
		if (this.attacksReceived == REGULAR_ATTACKS) base = (int) (base * 0.5);
		else if (this.attacksReceived == LINKED_MOVES) base = (int) (base * 1.5);
		return base;
	}

	/** @return The direction this Pokémon is facing. */
	public Direction facing()
	{
		return this.facing;
	}

	public ArrayList<DungeonEvent> gainExperience(ExperienceGainedEvent event)
	{
		return this.usedPokemon.gainExperience(event);
	}

	public BaseStats getBaseStats()
	{
		return this.usedPokemon.getBaseStats();
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

	public ItemStack getItem()
	{
		return this.usedPokemon.getItem();
	}

	@Override
	public ItemStack getItem(int index)
	{
		return this.usedPokemon.getItem(index);
	}

	public int getMaxHP()
	{
		return this.getBaseStats().getHealth();
	}

	public Message getNickname()
	{
		return this.usedPokemon.getNickname();
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

	public void increaseIQ(int iq)
	{
		this.usedPokemon.increaseIQ(iq);
	}

	public void inflictStatusCondition(StatusConditionInstance condition)
	{
		this.statusConditions.add(condition);
	}

	public boolean isAlliedWith(DungeonPokemon pokemon)
	{
		if (this == pokemon) return true;
		return this.player() == pokemon.player();
	}

	public boolean isBellyFull()
	{
		return this.getBelly() == this.getBellySize();
	}

	/** @return True if this Pokémon is the original Pokémon that visits this Dungeon. Only false for Dungeons that modify the visiting Pokémon, such as Dungeons resetting the level to 1. */
	public boolean isCopy()
	{
		return this.usedPokemon != this.originalPokemon;
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
		return this.player() != null && this.player().getTeamLeader() == this.originalPokemon;
	}

	@Override
	public ArrayList<ItemAction> legalItemActions()
	{
		return this.usedPokemon.legalItemActions();
	}

	public int level()
	{
		return this.usedPokemon.level();
	}

	public LearnedMove move(int slot)
	{
		return this.usedPokemon.move(slot);
	}

	public int moveCount()
	{
		return this.usedPokemon.moveCount();
	}

	/** Called when this Pokémon enters a new Floor or when it spawns. */
	public ArrayList<DungeonEvent> onFloorStart(Floor floor)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		this.statusConditions.clear();
		this.regenCounter = 0;
		return events;
	}

	/** Called at the beginning of each turn. */
	public void onTurnStart(Floor floor, ArrayList<DungeonEvent> events)
	{
		if (this.canRegen())
		{
			int recoveryRate = 200;
			int healthGain = 0;

			this.regenCounter += this.regenCounter + this.getBaseStats().health;
			healthGain += this.getBaseStats().health / recoveryRate;
			if (this.regenCounter >= recoveryRate)
			{
				healthGain += 1;
				this.regenCounter %= recoveryRate;
			}
			this.setHP(this.getHp() + healthGain);
		}

		if (this.stats.speedBuffs() > 0 || this.stats.speedDebuffs() > 0) this.stats.onTurnStart(floor, events);

		for (StatusConditionInstance condition : this.statusConditions)
			events.addAll(condition.tick(floor));
	}

	public Player player()
	{
		return this.usedPokemon.player();
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
	}

	public void setHP(int hp)
	{
		this.hp = hp;
		if (this.hp < 0) this.hp = 0;
		if (this.hp > this.getBaseStats().health) this.hp = this.getBaseStats().health;
	}

	@Override
	public void setItem(int index, ItemStack item)
	{
		this.usedPokemon.setItem(index, item);
		if (this.isCopy()) this.originalPokemon.setItem(index, item);
	}

	public void setItem(ItemStack item)
	{
		this.usedPokemon.setItem(item);
		this.originalPokemon.setItem(item);
	}

	public void setMove(int slot, LearnedMove move)
	{
		this.usedPokemon.setMove(slot, move);
	}

	public void setNickname(String nickname)
	{
		this.usedPokemon.setNickname(nickname);
		this.originalPokemon.setNickname(nickname);
	}

	public void setTile(Tile tile)
	{
		this.tile = tile;
	}

	@Override
	public int size()
	{
		return this.usedPokemon.size();
	}

	public PokemonSpecies species()
	{
		return this.usedPokemon.species();
	}

	public void switchMoves(int slot1, int slot2)
	{
		this.usedPokemon.switchMoves(slot1, slot2);
	}

	public Tile tile()
	{
		return this.tile;
	}

	@Override
	public String toString()
	{
		return this.usedPokemon.toString();
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

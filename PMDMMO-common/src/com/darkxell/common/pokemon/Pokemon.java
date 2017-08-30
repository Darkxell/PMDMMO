package com.darkxell.common.pokemon;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.ability.Ability;

public class Pokemon
{
	/** Pokémon gender.
	 * <ul>
	 * <li>MALE = false</li>
	 * <li>FEMALE = true</li>
	 * </ul> */
	public static final boolean MALE = false, FEMALE = true;

	/** This Pokémon's ability's ID. */
	public final int abilityID;
	/** The current amount of experience of this Pokémon (for this level). */
	private int experience;
	/** This Pokémon's gender. See {@link Pokemon#MALE}. */
	public final boolean gender;
	/** This Pokémon's ID. */
	public final int id;
	/** This Pokémon's held Item's ID. -1 for no Item. */
	private ItemStack item;
	/** This Pokémon's level. */
	private int level;
	/** ID of this Pokémon's moves. -1 for no move. */
	private PokemonMove[] moves;
	/** This Pokémon's nickname. If null, use the species' name. */
	private String nickname;
	/** This Pokémon's species. */
	public final PokemonSpecies species;
	/** This Pokémon's stats. */
	private PokemonStats stats;

	public Pokemon(int id, PokemonSpecies species, String nickname, ItemStack item, PokemonStats stats, int ability, int experience, int level,
			PokemonMove move1, PokemonMove move2, PokemonMove move3, PokemonMove move4, boolean gender)
	{
		this.id = id;
		this.species = species;
		this.nickname = nickname;
		this.item = item;
		this.stats = stats;
		this.abilityID = ability;
		this.experience = experience;
		this.level = level;
		this.moves = new PokemonMove[]
		{ move1, move2, move3, move4 };
		this.gender = gender;
	}

	/** @param amount - The amount of experience gained.
	 * @return The number of levels this experience granted. */
	public int gainExperience(int amount)
	{
		int levelups = 0;

		while (amount != 0)
		{
			int next = this.species.experienceToNextLevel(this.level) - this.experience;
			if (next <= amount)
			{
				amount -= next;
				this.experience = 0;
				++levelups;
				this.levelUp();
			} else
			{
				this.experience += amount;
				amount = 0;
			}
		}

		return levelups;
	}

	public Ability getAbility()
	{
		return Ability.find(this.abilityID);
	}

	public int getExperience()
	{
		return this.experience;
	}

	public ItemStack getItem()
	{
		return this.item;
	}

	public int getLevel()
	{
		return this.level;
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public PokemonStats getStats()
	{
		return this.stats;
	}

	private void levelUp()
	{
		++this.level;
		this.stats = this.species.stats.forLevel(this.level);
	}

	public PokemonMove move(int slot)
	{
		if (slot < 0 || slot >= this.moves.length) return null;
		return this.moves[slot];
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	public void setMove(int slot, PokemonMove move)
	{
		if (slot >= 0 && slot < this.moves.length) this.moves[slot] = move;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public void switchMoves(int slot1, int slot2)
	{
		if (slot1 < 0 || slot1 >= this.moves.length || slot2 < 0 || slot2 >= this.moves.length) return;
		PokemonMove temp = this.move(slot1);
		this.moves[slot1] = this.move(slot2);
		this.moves[slot2] = temp;
	}

	public int totalExperience()
	{
		int xp = this.experience;
		for (int lvl = 1; lvl < this.level; ++lvl)
			xp += this.species.experienceToNextLevel(lvl);
		return xp;
	}

}

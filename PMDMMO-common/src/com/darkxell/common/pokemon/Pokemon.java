package com.darkxell.common.pokemon;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.ability.Ability;

public class Pokemon
{
	/** Pok�mon gender.
	 * <ul>
	 * <li>MALE = false</li>
	 * <li>FEMALE = true</li>
	 * </ul> */
	public static final boolean MALE = false, FEMALE = true;

	/** This Pok�mon's ability's ID. */
	public final int abilityID;
	/** The total amount of experience of this Pok�mon. */
	private int experience;
	/** This Pok�mon's gender. See {@link Pokemon#MALE}. */
	public final boolean gender;
	/** This Pok�mon's ID. */
	public final int id;
	/** This Pok�mon's held Item's ID. -1 for no Item. */
	private ItemStack item;
	/** This Pok�mon's level. */
	private int level;
	/** ID of this Pok�mon's moves. -1 for no move. */
	private PokemonMove[] moves;
	/** This Pok�mon's nickname. If null, use the species' name. */
	private String nickname;
	/** This Pok�mon's species. */
	public final PokemonSpecies species;
	/** This Pok�mon's stats. */
	public final PokemonStats stats;

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

}

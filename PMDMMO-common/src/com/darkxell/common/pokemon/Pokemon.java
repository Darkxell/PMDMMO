package com.darkxell.common.pokemon;

public class Pokemon
{
	/** Pokémon gender.
	 * <ul>
	 * <li>MALE = false</li>
	 * <li>FEMALE = true</li>
	 * </ul> */
	public static final boolean MALE = false, FEMALE = true;

	/** The total amount of experience of this Pokémon. */
	private int experience;
	/** This Pokémon's gender. See {@link Pokemon#MALE}. */
	public final boolean gender;
	/** This Pokémon's ID. */
	public final int id;
	/** This Pokémon's IVs. */
	public final PokemonStats individualValues;
	/** This Pokémon's held Item's ID. -1 for no Item. */
	private int item;
	/** This Pokémon's level. */
	private int level;
	/** ID of this Pokémon's moves. -1 for no move. */
	private int move1, move2, move3, move4;
	/** This Pokémon's nickname. If null, use the species' name. */
	private String nickname;
	/** This Pokémon's species. */
	public final PokemonSpecies species;

	public Pokemon(int id, PokemonSpecies species, String nickname, int item, PokemonStats individualValues, int experience, int level, int move1, int move2,
			int move3, int move4, boolean gender)
	{
		this.id = id;
		this.species = species;
		this.nickname = nickname;
		this.item = item;
		this.individualValues = individualValues;
		this.experience = experience;
		this.level = level;
		this.move1 = move1;
		this.move2 = move2;
		this.move3 = move3;
		this.move4 = move4;
		this.gender = gender;
	}

	public int getExperience()
	{
		return this.experience;
	}

	public int getItem()
	{
		return this.item;
	}

	public int getLevel()
	{
		return this.level;
	}

	public int getMove1()
	{
		return this.move1;
	}

	public int getMove2()
	{
		return this.move2;
	}

	public int getMove3()
	{
		return this.move3;
	}

	public int getMove4()
	{
		return this.move4;
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public void setItem(int item)
	{
		this.item = item;
	}

	public void setMove1(int move1)
	{
		this.move1 = move1;
	}

	public void setMove2(int move2)
	{
		this.move2 = move2;
	}

	public void setMove3(int move3)
	{
		this.move3 = move3;
	}

	public void setMove4(int move4)
	{
		this.move4 = move4;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

}

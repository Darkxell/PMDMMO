package com.darkxell.common.pokemon;

public class DungeonStats
{
	public static final int[] attackTable = new int[] { 64, 69, 74, 79, 84, 89, 102, 115, 128, 179, 256, 332, 384, 406, 422, 435, 448, 460, 473, 486, 512 };
	public static final float DEFAULT_ACCURACY = 1, DEFAULT_EVASIVENESS = 0;
	public static final int[] defenseTable = new int[] { 64, 69, 74, 79, 84, 89, 102, 140, 179, 222, 256, 332, 384, 409, 422, 435, 448, 460, 473, 486, 512 };
	public static final float[] speedTable = { .5f, 1f, 2f, 3f, 4f };

	/** Accuracy. */
	private float accuracy;
	/** Attack. */
	private int attack;
	/** The base stats. */
	public final PokemonStats baseStats;
	/** Defense. */
	private int defense;
	/** Evasiveness. */
	private float evasiveness;
	/** Health Points. */
	private int health;
	/** Owner of these stats. */
	public final DungeonPokemon pokemon;
	/** Special Attack. */
	private int specialAttack;
	/** Special Defense. */
	private int specialDefense;
	int speedRecharge = 0;
	private int[] stages = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };

	public DungeonStats(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;
		this.baseStats = this.pokemon.getStats();
		this.health = this.baseStats.health;
		this.onStatChange();
		this.resetStages();
	}

	public void addStage(int stat, int stage)
	{
		this.setStage(stat, this.getStage(stat) + stage);
	}

	public float getAccuracy()
	{
		return this.accuracy;
	}

	public int getAttack()
	{
		return this.attack * attackTable[this.stages[PokemonStats.ATTACK]] / 256;
	}

	public int getDefense()
	{
		return this.defense * defenseTable[this.stages[PokemonStats.DEFENSE]] / 256;
	}

	public float getEvasiveness()
	{
		return this.evasiveness;
	}

	public int getHealth()
	{
		return this.health;
	}

	public float getMoveSpeed()
	{
		return speedTable[this.stages[PokemonStats.SPEED]];
	}

	public int getSpecialAttack()
	{
		return this.specialAttack * attackTable[this.stages[PokemonStats.SPECIAL_ATTACK]] / 256;
	}

	public int getSpecialDefense()
	{
		return this.specialDefense * defenseTable[this.stages[PokemonStats.SPECIAL_DEFENSE]] / 256;
	}

	public int getStage(int stat)
	{
		return this.stages[stat];
	}

	/** Called when the base stats change. */
	public void onStatChange()
	{
		int hpchange = this.baseStats.health - this.health;
		this.attack = this.baseStats.attack;
		this.defense = this.baseStats.defense;
		this.health = this.baseStats.health;
		this.specialAttack = this.baseStats.specialAttack;
		this.specialDefense = this.baseStats.specialDefense;
		this.evasiveness = DEFAULT_EVASIVENESS;
		this.accuracy = DEFAULT_ACCURACY;
		this.pokemon.setHP(this.pokemon.getHp() + hpchange);
	}

	/** Called when the Pokémon steps on a Wonder Tile or changes Floor. */
	public void resetStages()
	{
		for (int i = 0; i < this.stages.length; i++)
			this.stages[i] = 10;
		this.stages[PokemonStats.SPEED] = 1;
	}

	public void setStage(int stat, int stage)
	{
		if (stage < 0) stage = 0;
		if (stat == PokemonStats.SPEED)
		{
			if (stage >= speedTable.length) stage = speedTable.length - 1;
			if (stage == 0) this.speedRecharge = 8;
		} else if (stage > 20) stage = 20;

		if (stat >= 0 && stat < this.stages.length) this.stages[stat] = stage;
	}

}

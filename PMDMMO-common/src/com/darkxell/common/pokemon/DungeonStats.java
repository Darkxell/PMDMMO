package com.darkxell.common.pokemon;

public class DungeonStats
{
	public static final float DEFAULT_ACCURACY = 1, DEFAULT_EVASIVENESS = 0;

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
	/** Movement Speed. */
	private float moveSpeed;
	/** Special Attack. */
	private int specialAttack;
	/** Special Defense. */
	private int specialDefense;
	/** Speed. */
	private int speed;

	public DungeonStats(PokemonStats baseStats)
	{
		this.baseStats = baseStats;
		this.resetStats();
	}

	public float getAccuracy()
	{
		return this.accuracy;
	}

	public int getAttack()
	{
		return this.attack;
	}

	public int getDefense()
	{
		return this.defense;
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
		return this.moveSpeed;
	}

	public int getSpecialAttack()
	{
		return this.specialAttack;
	}

	public int getSpecialDefense()
	{
		return this.specialDefense;
	}

	public int getSpeed()
	{
		return this.speed;
	}

	/** Resets these Stats to their default input values. */
	public void resetStats()
	{
		this.attack = this.baseStats.attack;
		this.defense = this.baseStats.defense;
		this.health = this.baseStats.health;
		this.specialAttack = this.baseStats.specialAttack;
		this.specialDefense = this.baseStats.specialDefense;
		this.speed = this.baseStats.speed;
		this.moveSpeed = this.baseStats.moveSpeed;
		this.evasiveness = DEFAULT_EVASIVENESS;
		this.accuracy = DEFAULT_ACCURACY;
	}

	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	public void setAttack(int attack)
	{
		this.attack = attack;
	}

	public void setDefense(int defense)
	{
		this.defense = defense;
	}

	public void setEvasiveness(float evasiveness)
	{
		this.evasiveness = evasiveness;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public void setMoveSpeed(float moveSpeed)
	{
		this.moveSpeed = moveSpeed;
	}

	public void setSpecialAttack(int specialAttack)
	{
		this.specialAttack = specialAttack;
	}

	public void setSpecialDefense(int specialDefense)
	{
		this.specialDefense = specialDefense;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

}

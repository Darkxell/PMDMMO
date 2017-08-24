package com.darkxell.common.pokemon;

public class PokemonStats
{

	/** Attack. */
	public final int attack;
	/** Defense. */
	public final int defense;
	/** Health Points. */
	public final int health;
	/** Special Attack. */
	public final int specialAttack;
	/** Special Defense. */
	public final int specialDefense;
	/** Speed. */
	public final int speed;

	public PokemonStats(int attack, int defense, int health, int specialAttack, int specialDefense, int speed)
	{
		this.attack = attack;
		this.defense = defense;
		this.health = health;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.speed = speed;
	}

}

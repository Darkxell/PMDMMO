package com.darkxell.common.pokemon;

import org.jdom2.Element;

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

	public Element toXML()
	{
		Element root = new Element("stats");
		root.setAttribute("atk", Integer.toString(this.attack));
		root.setAttribute("def", Integer.toString(this.defense));
		root.setAttribute("hea", Integer.toString(this.health));
		root.setAttribute("spa", Integer.toString(this.specialAttack));
		root.setAttribute("spd", Integer.toString(this.specialDefense));
		root.setAttribute("spe", Integer.toString(this.speed));
		return root;
	}

}

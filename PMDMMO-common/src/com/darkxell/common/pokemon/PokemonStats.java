package com.darkxell.common.pokemon;

import org.jdom2.Element;

public class PokemonStats
{
	/** Stat IDs.<br />
	 * <ul>
	 * <li>ATTACK = 0</li>
	 * <li>DEFENSE = 1</li>
	 * <li>HEALTH = 2</li>
	 * <li>SPECIAL_ATTACK = 3</li>
	 * <li>SPECIAL_DEFENSE = 4</li>
	 * <li>SPEED = 5</li>
	 * <li>EVASION = 6</li>
	 * </ul> */
	public static final byte ATTACK = 0, DEFENSE = 1, HEALTH = 2, SPECIAL_ATTACK = 3, SPECIAL_DEFENSE = 4, SPEED = 5, EVASION = 6;

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

	public PokemonStats(Element xml)
	{
		this.attack = Integer.parseInt(xml.getAttributeValue("atk"));
		this.defense = Integer.parseInt(xml.getAttributeValue("def"));
		this.health = Integer.parseInt(xml.getAttributeValue("hea"));
		this.specialAttack = Integer.parseInt(xml.getAttributeValue("spa"));
		this.specialDefense = Integer.parseInt(xml.getAttributeValue("spd"));
		this.speed = Integer.parseInt(xml.getAttributeValue("spe"));
	}

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

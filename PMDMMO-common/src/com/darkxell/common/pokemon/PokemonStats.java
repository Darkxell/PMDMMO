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
	 * <li>ACCURACY = 7</li>
	 * </ul> */
	public static final byte ATTACK = 0, DEFENSE = 1, HEALTH = 2, SPECIAL_ATTACK = 3, SPECIAL_DEFENSE = 4, SPEED = 5, EVASIVENESS = 6, ACCURACY = 7;
	public static final String XML_ROOT = "stats";

	/** Attack. */
	int attack;
	/** Defense. */
	int defense;
	/** Health Points. */
	int health;
	/** Movement Speed. */
	int moveSpeed;
	/** Special Attack. */
	int specialAttack;
	/** Special Defense. */
	int specialDefense;

	public PokemonStats(Element xml)
	{
		this.attack = xml.getAttribute("atk") == null ? 0 : Integer.parseInt(xml.getAttributeValue("atk"));
		this.defense = xml.getAttribute("def") == null ? 0 : Integer.parseInt(xml.getAttributeValue("def"));
		this.health = xml.getAttribute("hea") == null ? 0 : Integer.parseInt(xml.getAttributeValue("hea"));
		this.specialAttack = xml.getAttribute("spa") == null ? 0 : Integer.parseInt(xml.getAttributeValue("spa"));
		this.specialDefense = xml.getAttribute("spd") == null ? 0 : Integer.parseInt(xml.getAttributeValue("spd"));
		this.moveSpeed = xml.getAttribute("msp") == null ? 1 : Integer.parseInt(xml.getAttributeValue("msp"));
	}

	public PokemonStats(int attack, int defense, int health, int specialAttack, int specialDefense, int moveSpeed)
	{
		this.attack = attack;
		this.defense = defense;
		this.health = health;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.moveSpeed = moveSpeed;
	}

	/** Adds the input stats to these stats. */
	public void add(PokemonStats stats)
	{
		this.attack += stats.attack;
		this.attack += stats.defense;
		this.health += stats.health;
		this.specialAttack += stats.specialAttack;
		this.specialDefense += stats.specialDefense;
	}

	public int getAttack()
	{
		return this.attack;
	}

	public int getDefense()
	{
		return this.defense;
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

	public Element toXML()
	{
		Element root = new Element("stats");
		if (this.attack != 0) root.setAttribute("atk", Integer.toString(this.attack));
		if (this.defense != 0) root.setAttribute("def", Integer.toString(this.defense));
		if (this.health != 0) root.setAttribute("hea", Integer.toString(this.health));
		if (this.specialAttack != 0) root.setAttribute("spa", Integer.toString(this.specialAttack));
		if (this.specialDefense != 0) root.setAttribute("spd", Integer.toString(this.specialDefense));
		if (this.moveSpeed != 1) root.setAttribute("msp", Float.toString(this.moveSpeed));
		return root;
	}

}

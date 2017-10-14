package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

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
		this.attack = XMLUtils.getAttribute(xml, "atk", 0);
		this.defense = XMLUtils.getAttribute(xml, "def", 0);
		this.health = XMLUtils.getAttribute(xml, "hea", 0);
		this.specialAttack = XMLUtils.getAttribute(xml, "spa", 0);
		this.specialDefense = XMLUtils.getAttribute(xml, "spd", 0);
		this.moveSpeed = XMLUtils.getAttribute(xml, "msp", 1);
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

	public PokemonStats(int[] stat)
	{
		this.attack = stat[ATTACK];
		this.defense = stat[DEFENSE];
		this.health = stat[HEALTH];
		this.specialAttack = stat[SPECIAL_ATTACK];
		this.specialDefense = stat[SPECIAL_DEFENSE];
		this.moveSpeed = stat.length == 5 ? 1 : stat[SPEED];
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

	public int getMoveSpeed()
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
		XMLUtils.setAttribute(root, "atk", this.attack, 0);
		XMLUtils.setAttribute(root, "def", this.defense, 0);
		XMLUtils.setAttribute(root, "hea", this.health, 0);
		XMLUtils.setAttribute(root, "spa", this.specialAttack, 0);
		XMLUtils.setAttribute(root, "spd", this.specialDefense, 0);
		XMLUtils.setAttribute(root, "msp", this.moveSpeed, 1);
		return root;
	}

}

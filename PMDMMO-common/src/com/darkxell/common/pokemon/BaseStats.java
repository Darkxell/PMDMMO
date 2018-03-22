package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public class BaseStats
{
	public static enum Stat
	{
		Accuracy(6),
		Attack(0),
		Defense(1),
		Evasiveness(5),
		Health(2),
		SpecialAttack(3),
		SpecialDefense(4),
		Speed(7);

		public final int id;

		private Stat(int id)
		{
			this.id = id;
		}

	}public static final String XML_ROOT = "stats";

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

	public BaseStats(Element xml)
	{
		this.attack = XMLUtils.getAttribute(xml, "atk", 0);
		this.defense = XMLUtils.getAttribute(xml, "def", 0);
		this.health = XMLUtils.getAttribute(xml, "hea", 0);
		this.specialAttack = XMLUtils.getAttribute(xml, "spa", 0);
		this.specialDefense = XMLUtils.getAttribute(xml, "spd", 0);
		this.moveSpeed = XMLUtils.getAttribute(xml, "msp", 1);
	}

	public BaseStats(int attack, int defense, int health, int specialAttack, int specialDefense, int moveSpeed)
	{
		this.attack = attack;
		this.defense = defense;
		this.health = health;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.moveSpeed = moveSpeed;
	}

	public BaseStats(int[] stat)
	{
		this.attack = stat[Stat.Attack.id];
		this.defense = stat[Stat.Defense.id];
		this.health = stat[Stat.Health.id];
		this.specialAttack = stat[Stat.SpecialAttack.id];
		this.specialDefense = stat[Stat.SpecialDefense.id];
		this.moveSpeed = stat.length == 5 ? 1 : stat[5];
	}

	/** Adds the input stats to these stats. */
	public void add(BaseStats stats)
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

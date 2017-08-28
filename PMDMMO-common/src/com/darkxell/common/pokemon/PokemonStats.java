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
	 * <li>MOVEMENT_SPEED = 8</li>
	 * </ul> */
	public static final byte ATTACK = 0, DEFENSE = 1, HEALTH = 2, SPECIAL_ATTACK = 3, SPECIAL_DEFENSE = 4, SPEED = 5, EVASIVENESS = 6, ACCURACY = 7,
			MOVEMENT_SPEED = 8;

	/** Attack. */
	public final int attack;
	/** Defense. */
	public final int defense;
	/** Health Points. */
	public final int health;
	/** Movement Speed. */
	public final float moveSpeed;
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
		this.moveSpeed = xml.getAttribute("msp") == null ? 1 : Float.parseFloat(xml.getAttributeValue("spe"));
	}

	public PokemonStats(int attack, int defense, int health, int specialAttack, int specialDefense, int speed, float moveSpeed)
	{
		this.attack = attack;
		this.defense = defense;
		this.health = health;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.speed = speed;
		this.moveSpeed = moveSpeed;
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
		if (this.moveSpeed != 1) root.setAttribute("msp", Float.toString(this.moveSpeed));
		return root;
	}

}

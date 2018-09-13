package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class BaseStats implements Communicable
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

		public final static int MAX_SPEED = 4;

		public final int id;

		private Stat(int id)
		{
			this.id = id;
		}

		public Message getName()
		{
			return new Message("stat." + this.name());
		}

	}

	public static final String XML_ROOT = "stats";

	/** Attack. */
	int attack;
	/** Defense. */
	int defense;
	/** Health Points. */
	int health;
	/** Movement Speed. */
	int moveSpeed;
	public final Pokemon pokemon;
	/** Special Attack. */
	int specialAttack;
	/** Special Defense. */
	int specialDefense;

	public BaseStats(Pokemon pokemon, int attack, int defense, int health, int specialAttack, int specialDefense, int moveSpeed)
	{
		this.pokemon = pokemon;
		this.attack = attack;
		this.defense = defense;
		this.health = health;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.moveSpeed = moveSpeed;
	}

	public BaseStats(Pokemon pokemon, int[] stat)
	{
		this.pokemon = pokemon;
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

		this.onStatsChange();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof BaseStats)) return false;
		BaseStats s = (BaseStats) obj;
		return this.attack == s.attack && this.defense == s.defense && this.health == s.health && this.moveSpeed == s.moveSpeed
				&& this.specialAttack == s.specialAttack && this.specialDefense == s.specialDefense;
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

	private void onStatsChange()
	{
		if (this.pokemon == null) return;
		this.pokemon.getData().stat_atk = this.attack;
		this.pokemon.getData().stat_def = this.defense;
		this.pokemon.getData().stat_speatk = this.specialAttack;
		this.pokemon.getData().stat_spedef = this.specialDefense;
		this.pokemon.getData().stat_hp = this.health;
	}

	@Override
	public void read(JsonObject value)
	{
		this.attack = value.getInt("atk", 0);
		this.defense = value.getInt("def", 0);
		this.health = value.getInt("hea", 0);
		this.specialAttack = value.getInt("spa", 0);
		this.specialDefense = value.getInt("spd", 0);
		this.moveSpeed = value.getInt("msp", 1);
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("atk", this.attack);
		root.add("def", this.defense);
		root.add("hea", this.health);
		root.add("spa", this.specialAttack);
		root.add("spd", this.specialDefense);
		if (this.moveSpeed != 1) root.add("msp", this.moveSpeed);
		return root;
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

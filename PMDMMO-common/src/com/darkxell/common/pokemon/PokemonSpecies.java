package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.TileType.Mobility;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class PokemonSpecies
{
	public static final int COMPOUND_ID_FACTOR = 10000;
	public static final double SHINY_CHANCE = 1d / 8192;
	// public static final double SHINY_CHANCE = .5; // Used to test shiny pokémon

	/** List of possible Abilities for this Pokémon. */
	private ArrayList<Integer> abilities;
	/** This species' stat gains at each level. First in this Array is the stats at level 1. */
	private final ArrayList<BaseStats> baseStats;
	/** Base experience gained when this Pokémon is defeated. */
	public final int baseXP;
	/** List of species this Pokémon can evolve into. */
	private final ArrayList<Evolution> evolutions;
	/** Lists the required experience to level up for each level. */
	final int[] experiencePerLevel;
	/** The list of different forms Pokémon of this species can have. */
	private final ArrayList<PokemonSpecies> forms;
	public final String friendAreaID;
	public final float height, weight;
	public final int id, formID;
	/** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
	private final HashMap<Integer, ArrayList<Integer>> learnset;
	public final Mobility mobility;
	/** List of TMs that can be taught. */
	private final ArrayList<Integer> tms;
	/** This Pokémon's types. type2 can be null. */
	public final PokemonType type1, type2;

	/** Constructor for XML */
	public PokemonSpecies(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.formID = XMLUtils.getAttribute(xml, "form-id", 0);
		this.type1 = PokemonType.valueOf(xml.getAttributeValue("type1"));
		this.type2 = xml.getAttribute("type2") == null ? null : PokemonType.valueOf(xml.getAttributeValue("type2"));
		this.baseXP = Integer.parseInt(xml.getAttributeValue("base-xp"));
		this.height = Float.parseFloat(xml.getAttributeValue("height"));
		this.weight = Float.parseFloat(xml.getAttributeValue("weight"));
		this.abilities = XMLUtils.readIntArrayAsList(xml.getChild("abilities", xml.getNamespace()));
		this.mobility = xml.getAttribute("mobility") == null ? this.defaultMobility() : Mobility.valueOf(xml.getAttributeValue("mobility"));
		this.baseStats = new ArrayList<BaseStats>();
		this.learnset = new HashMap<Integer, ArrayList<Integer>>();
		this.tms = XMLUtils.readIntArrayAsList(xml.getChild("tms", xml.getNamespace()));
		this.evolutions = new ArrayList<Evolution>();

		if (xml.getChild("statline", xml.getNamespace()) != null)
		{
			int[][] statline = XMLUtils.readDoubleIntArray(xml.getChild("statline", xml.getNamespace()));
			for (int[] stat : statline)
				this.baseStats.add(new BaseStats(null, stat));
		}

		if (xml.getChild("experience", xml.getNamespace()) != null)
		{
			String[] lvls = xml.getChildText("experience", xml.getNamespace()).split(",");
			this.experiencePerLevel = new int[lvls.length];
			for (int lvl = 0; lvl < lvls.length; lvl++)
				this.experiencePerLevel[lvl] = Integer.parseInt(lvls[lvl]);
		} else this.experiencePerLevel = new int[0];

		if (xml.getChild("evolves", xml.getNamespace()) != null) for (Element e : xml.getChild("evolves", xml.getNamespace()).getChildren())
			this.evolutions.add(new Evolution(e));

		if (xml.getChild("tms", xml.getNamespace()) != null) for (Element tm : xml.getChild("tms", xml.getNamespace()).getChildren())
			this.tms.add(Integer.parseInt(tm.getAttributeValue("id")));

		if (xml.getChild("learnset", xml.getNamespace()) != null) for (Element level : xml.getChild("learnset", xml.getNamespace()).getChildren())
			this.learnset.put(Integer.parseInt(level.getAttributeValue("l")), XMLUtils.readIntArrayAsList(level));

		this.forms = new ArrayList<PokemonSpecies>();
		for (Element form : xml.getChildren("form", xml.getNamespace()))
			this.forms.add(createForm(form));

		this.friendAreaID = xml.getAttributeValue("area");
	}

	public PokemonSpecies(int id, int formID, PokemonType type1, PokemonType type2, int baseXP, ArrayList<BaseStats> baseStats, float height, float weight,
			Mobility mobility, ArrayList<Integer> abilities, int[] experiencePerLevel, HashMap<Integer, ArrayList<Integer>> learnset, ArrayList<Integer> tms,
			ArrayList<Evolution> evolutions, ArrayList<PokemonSpecies> forms, String friendAreaID)
	{
		this.id = id;
		this.formID = formID;
		this.type1 = type1;
		this.type2 = type2;
		this.baseXP = baseXP;
		this.baseStats = baseStats;
		this.height = height;
		this.weight = weight;
		this.mobility = mobility;
		this.abilities = abilities;
		this.experiencePerLevel = experiencePerLevel;
		this.learnset = learnset;
		this.tms = tms;
		this.evolutions = evolutions;
		this.forms = forms;
		this.friendAreaID = friendAreaID;
	}

	public BaseStats baseStatsIncrease(int level)
	{
		return this.baseStats.get(level);
	}

	public int compoundID()
	{
		if (this.formID == 0) return this.id;
		return this.id * COMPOUND_ID_FACTOR + this.formID;
	}

	@SuppressWarnings("unchecked")
	private PokemonSpecies createForm(Element xml)
	{
		int formID = XMLUtils.getAttribute(xml, "form", 0);

		PokemonType type1 = xml.getAttribute("type1") == null ? this.type1 : PokemonType.valueOf(xml.getAttributeValue("type1"));
		PokemonType type2 = xml.getAttribute("type2") == null ? this.type2
				: xml.getAttributeValue("type2").equals("null") ? null : PokemonType.valueOf(xml.getAttributeValue("type2"));
		int baseXP = XMLUtils.getAttribute(xml, "base-xp", this.baseXP);
		float height = XMLUtils.getAttribute(xml, "height", this.height);
		float weight = XMLUtils.getAttribute(xml, "weight", this.weight);
		Mobility mobility = xml.getAttribute("mobility") == null ? this.mobility : Mobility.valueOf(xml.getAttributeValue("mobility"));
		ArrayList<Integer> abilities = xml.getChild("abilities", xml.getNamespace()) == null ? (ArrayList<Integer>) this.abilities.clone()
				: XMLUtils.readIntArrayAsList(xml.getChild("abilities", xml.getNamespace()));
		ArrayList<BaseStats> baseStats = new ArrayList<BaseStats>();
		HashMap<Integer, ArrayList<Integer>> learnset = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> tms = xml.getChild("tms", xml.getNamespace()) == null ? (ArrayList<Integer>) this.tms.clone()
				: XMLUtils.readIntArrayAsList(xml.getChild("tms", xml.getNamespace()));
		ArrayList<Evolution> evolutions = new ArrayList<Evolution>();

		if (xml.getChild("statline", xml.getNamespace()) == null) baseStats = (ArrayList<BaseStats>) this.baseStats.clone();
		else
		{
			int[][] statline = XMLUtils.readDoubleIntArray(xml.getChild("statline", xml.getNamespace()));
			for (int[] stat : statline)
				baseStats.add(new BaseStats(null, stat));
		}

		int[] experiencePerLevel;
		if (xml.getChild("experience", xml.getNamespace()) == null) experiencePerLevel = this.experiencePerLevel.clone();
		else
		{
			String[] lvls = xml.getChildText("experience", xml.getNamespace()).split(",");
			experiencePerLevel = new int[lvls.length];
			for (int lvl = 0; lvl < lvls.length; lvl++)
				experiencePerLevel[lvl] = Integer.parseInt(lvls[lvl]);
		}

		if (xml.getChild("evolves", xml.getNamespace()) == null) evolutions = (ArrayList<Evolution>) this.evolutions.clone();
		else for (Element e : xml.getChild("evolves", xml.getNamespace()).getChildren())
			evolutions.add(new Evolution(e));

		if (xml.getChild("tms", xml.getNamespace()) == null) tms = (ArrayList<Integer>) this.tms.clone();
		else for (Element tm : xml.getChild("tms", xml.getNamespace()).getChildren())
			tms.add(Integer.parseInt(tm.getAttributeValue("id")));

		if (xml.getChild("learnset", xml.getNamespace()) == null) learnset = (HashMap<Integer, ArrayList<Integer>>) this.learnset.clone();
		else for (Element level : xml.getChild("learnset", xml.getNamespace()).getChildren())
			learnset.put(Integer.parseInt(level.getAttributeValue("l")), XMLUtils.readIntArrayAsList(level));

		String friendArea = XMLUtils.getAttribute(xml, "area", this.friendAreaID);

		return new PokemonSpecies(this.id, formID, type1, type2, baseXP, baseStats, height, weight, mobility, abilities, experiencePerLevel, learnset, tms,
				evolutions, forms, friendArea);
	}

	private Mobility defaultMobility()
	{
		if (this.isType(PokemonType.Ghost)) return Mobility.Ghost;
		if (this.isType(PokemonType.Flying) || this.abilities.contains(Ability.LEVITATE.id)) return Mobility.Flying;
		if (this.isType(PokemonType.Fire)) return Mobility.Fire;
		if (this.isType(PokemonType.Water)) return Mobility.Water;
		return Mobility.Normal;
	}

	public Evolution[] evolutions()
	{
		return this.evolutions.toArray(new Evolution[this.evolutions.size()]);
	}

	/** @return The amount of experience needed to level up from the input level. */
	public int experienceToNextLevel(int level)
	{
		return this.experiencePerLevel[level - 1];
	}

	/** @return This form's name. */
	public Message formName()
	{
		if (this.forms.isEmpty()) return this.speciesName();
		return new Message("pokemon." + this.id + "." + this.formID);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PokemonSpecies> forms()
	{
		return (ArrayList<PokemonSpecies>) this.forms.clone();
	}

	/** Generates a Pokémon of this species.
	 * 
	 * @param level - The level of the Pokémon to generate. */
	public Pokemon generate(Random random, int level)
	{
		return this.generate(random, level, SHINY_CHANCE);
	}

	/** Generates a Pokémon of this species.
	 * 
	 * @param level - The level of the Pokémon to generate.
	 * @param shinyChance - The chance for the generated Pokémon to be a shiny (0 to 1). */
	public Pokemon generate(Random random, int level, double shinyChance)
	{
		ArrayList<Integer> moves = new ArrayList<Integer>();

		int m1 = this.latestMove(level, moves);
		moves.add(m1);
		int m2 = this.latestMove(level, moves);
		moves.add(m2);
		int m3 = this.latestMove(level, moves);
		moves.add(m3);
		int m4 = this.latestMove(level, moves);

		LearnedMove move1 = m1 == -1 ? null : new LearnedMove(m1);
		LearnedMove move2 = m2 == -1 ? null : new LearnedMove(m2);
		LearnedMove move3 = m3 == -1 ? null : new LearnedMove(m3);
		LearnedMove move4 = m4 == -1 ? null : new LearnedMove(m4);

		return new Pokemon(0, this, null, null, this.statsForLevel(level), this.randomAbility(random), 0, level, move1, move2, move3, move4,
				this.randomGender(random), 0, random.nextDouble() <= shinyChance);
	}

	/** @return True if one of this Pokémon's type equals the input type. */
	public boolean isType(PokemonType type)
	{
		return this.type1 == type || this.type2 == type;
	}

	/** @param level - The level of the Pokémon.
	 * @param learnedMoves - Moves to exclude because they're already learned.
	 * @return The latest learned move's ID. */
	public int latestMove(int level, ArrayList<Integer> learnedMoves)
	{
		while (level > 0)
		{
			if (this.learnset.containsKey(level))
			{
				@SuppressWarnings("unchecked")
				ArrayList<Integer> moves = (ArrayList<Integer>) this.learnset.get(level).clone();
				moves.removeAll(learnedMoves);
				if (moves.size() != 0 && MoveRegistry.find(moves.get(0)) != null) return moves.get(0);
			}
			--level;
		}
		return -1;
	}

	/** @return The list of learned moves at the input level. */
	public ArrayList<Move> learnedMoves(int level)
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		if (this.learnset.containsKey(level)) for (Integer id : this.learnset.get(level))
			moves.add(MoveRegistry.find(id));
		return moves;
	}

	/** @return A random ability for this Pokémon. */
	public int randomAbility(Random random)
	{
		if (this.abilities.size() == 0) return 0;
		return this.abilities.get(random.nextInt(this.abilities.size()));
	}

	/** @return A random gender for this Pokémon. */
	public byte randomGender(Random random)
	{
		// todo: include gender probability.
		return (byte) random.nextInt(3);
	}

	/** @return This species' name. */
	public Message speciesName()
	{
		return new Message("pokemon." + this.id);
	}

	/** @return Regular stats for a Pokémon at the input level. */
	public BaseStats statsForLevel(int level)
	{
		BaseStats stats = this.baseStats.get(0);
		for (int lvl = 1; lvl < level; ++lvl)
			stats.add(this.baseStats.get(lvl));
		return stats;
	}

	@Override
	public String toString()
	{
		return this.formName().toString();
	}

	public Element toXML()
	{
		Element root = new Element("pokemon");
		root.setAttribute("id", Integer.toString(this.id));
		XMLUtils.setAttribute(root, "form-id", this.formID, 0);
		root.setAttribute("type1", this.type1.name());
		if (this.type2 != null) root.setAttribute("type2", this.type2.name());
		root.setAttribute("base-xp", Integer.toString(this.baseXP));
		root.setAttribute("height", Float.toString(this.height));
		root.setAttribute("weight", Float.toString(this.weight));
		if (this.mobility != this.defaultMobility()) root.setAttribute("mobility", this.mobility.name());

		int[][] line = new int[100][];
		for (int lvl = 0; lvl < this.baseStats.size(); lvl++)
		{
			BaseStats stats = this.baseStats.get(lvl);
			if (stats.moveSpeed != 1) line[lvl] = new int[6];
			else line[lvl] = new int[5];

			line[lvl][Stat.Attack.id] = stats.getAttack();
			line[lvl][Stat.Defense.id] = stats.getDefense();
			line[lvl][Stat.Health.id] = stats.getHealth();
			line[lvl][Stat.SpecialAttack.id] = stats.getSpecialAttack();
			line[lvl][Stat.SpecialDefense.id] = stats.getSpecialDefense();
			if (stats.moveSpeed != 1) line[lvl][5] = stats.getMoveSpeed();
		}
		root.addContent(XMLUtils.toXML("statline", line));

		String s = "";
		for (int lvl = 0; lvl < this.experiencePerLevel.length; lvl++)
		{
			if (lvl != 0) s += ",";
			s += this.experiencePerLevel[lvl];
		}
		root.addContent(new Element("experience").setText(s));

		if (this.evolutions.size() != 0)
		{
			Element evolutions = new Element("evolves");
			for (Evolution evolution : this.evolutions)
				evolutions.addContent(evolution.toXML());
			root.addContent(evolutions);
		}

		if (this.abilities.size() != 0) root.addContent(XMLUtils.toXML("abilities", this.abilities));

		if (this.tms.size() != 0) root.addContent(XMLUtils.toXML("tms", this.tms));

		if (this.learnset.size() != 0)
		{
			Element learnset = new Element("learnset");
			for (Integer level : this.learnset.keySet())
			{
				Element lv = XMLUtils.toXML("level", this.learnset.get(level));
				lv.setAttribute("l", Integer.toString(level));
				learnset.addContent(lv);
			}
			root.addContent(learnset);
		}

		for (PokemonSpecies form : this.forms)
			this.toXML(root, form);
		
		root.setAttribute("area", this.friendAreaID);

		return root;
	}

	private void toXML(Element root, PokemonSpecies form)
	{
		Element e = new Element("form");
		e.setAttribute("id", Integer.toString(form.formID));
		if (this.type1 != form.type1) e.setAttribute("type1", form.type1.name());
		if (this.type2 != form.type2) e.setAttribute("type2", form.type2 == null ? "null" : form.type2.name());
		if (this.baseXP != form.baseXP) e.setAttribute("base-xp", Integer.toString(form.baseXP));
		if (this.height != form.height) e.setAttribute("height", Float.toString(form.height));
		if (this.weight != form.weight) e.setAttribute("weight", Float.toString(form.weight));
		if (this.mobility != form.mobility) e.setAttribute("mobility", form.mobility.name());

		if (!this.baseStats.equals(form.baseStats))
		{
			int[][] line = new int[100][];
			for (int lvl = 0; lvl < form.baseStats.size(); lvl++)
			{
				BaseStats stats = form.baseStats.get(lvl);
				if (stats.moveSpeed != 1) line[lvl] = new int[6];
				else line[lvl] = new int[5];

				line[lvl][Stat.Attack.id] = stats.getAttack();
				line[lvl][Stat.Defense.id] = stats.getDefense();
				line[lvl][Stat.Health.id] = stats.getHealth();
				line[lvl][Stat.SpecialAttack.id] = stats.getSpecialAttack();
				line[lvl][Stat.SpecialDefense.id] = stats.getSpecialDefense();
				if (stats.moveSpeed != 1) line[lvl][5] = stats.getMoveSpeed();
			}
			e.addContent(XMLUtils.toXML("statline", line));
		}

		{
			boolean flag = false;
			for (int i = 0; i < this.experiencePerLevel.length; ++i)
				if (this.experiencePerLevel[i] != form.experiencePerLevel[i])
				{
					flag = true;
					break;
				}
			if (flag)
			{
				String s = "";
				for (int lvl = 0; lvl < form.experiencePerLevel.length; lvl++)
				{
					if (lvl != 0) s += ",";
					s += form.experiencePerLevel[lvl];
				}
				e.addContent(new Element("experience").setText(s));
			}
		}

		if (!this.evolutions.equals(form.evolutions))
		{
			Element evolutions = new Element("evolves");
			for (Evolution evolution : form.evolutions)
				evolutions.addContent(evolution.toXML());
			e.addContent(evolutions);
		}

		if (!this.abilities.equals(form.abilities)) e.addContent(XMLUtils.toXML("abilities", form.abilities));

		if (!this.tms.equals(form.tms)) e.addContent(XMLUtils.toXML("tms", form.tms));

		if (!this.learnset.equals(form.learnset))
		{
			Element learnset = new Element("learnset");
			for (Integer level : form.learnset.keySet())
			{
				Element lv = XMLUtils.toXML("level", form.learnset.get(level));
				lv.setAttribute("l", Integer.toString(level));
				learnset.addContent(lv);
			}
			e.addContent(learnset);
		}
		
		XMLUtils.setAttribute(root, "area", form.friendAreaID, this.friendAreaID);
	}

}

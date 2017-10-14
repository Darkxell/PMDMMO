package com.darkxell.common.pokemon;

import java.util.*;

import org.jdom2.Element;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class PokemonSpecies
{

	/** List of possible Abilities for this Pokémon. */
	private ArrayList<Integer> abilities;
	/** This species' stat gains at each level. First in this Array is the stats at level 1. */
	private final ArrayList<PokemonStats> baseStats;
	/** Base experience gained when this Pokémon is defeated. */
	public final int baseXP;
	/** List of species this Pokémon can evolve into. */
	private final ArrayList<Evolution> evolutions;
	/** Lists the required experience to level up for each level. */
	final int[] experiencePerLevel;
	public final float height, weight;
	public final int id, formID;
	/** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
	private final HashMap<Integer, ArrayList<Integer>> learnset;
	/** List of TMs that can be taught. */
	private final ArrayList<Integer> tms;
	/** This Pokémon's types. type2 can be null. */
	public final PokemonType type1, type2;

	/** Constructor for XML */
	public PokemonSpecies(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.formID = XMLUtils.getAttribute(xml, "form-id", 0);
		this.type1 = PokemonType.find(Integer.parseInt(xml.getAttributeValue("type1")));
		this.type2 = xml.getAttribute("type2") == null ? null : PokemonType.find(Integer.parseInt(xml.getAttributeValue("type2")));
		this.baseXP = Integer.parseInt(xml.getAttributeValue("base-xp"));
		this.height = Float.parseFloat(xml.getAttributeValue("height"));
		this.weight = Float.parseFloat(xml.getAttributeValue("weight"));
		this.abilities = XMLUtils.readIntArrayAsList(xml.getChild("abilities"));
		this.baseStats = new ArrayList<PokemonStats>();
		this.learnset = new HashMap<Integer, ArrayList<Integer>>();
		this.tms = XMLUtils.readIntArrayAsList(xml.getChild("tms"));
		this.evolutions = new ArrayList<Evolution>();

		if (xml.getChild("statline") != null)
		{
			int[][] statline = XMLUtils.readDoubleIntArray(xml.getChild("statline"));
			for (int[] stat : statline)
				this.baseStats.add(new PokemonStats(stat));
		}

		if (xml.getChild("experience") != null)
		{
			String[] lvls = xml.getChildText("experience").split(",");
			this.experiencePerLevel = new int[lvls.length];
			for (int lvl = 0; lvl < lvls.length; lvl++)
				this.experiencePerLevel[lvl] = Integer.parseInt(lvls[lvl]);
		} else this.experiencePerLevel = new int[0];

		if (xml.getChild("evolves") != null) for (Element e : xml.getChild("evolves").getChildren())
			this.evolutions.add(new Evolution(e));

		if (xml.getChild("tms") != null) for (Element tm : xml.getChild("tms").getChildren())
			this.tms.add(Integer.parseInt(tm.getAttributeValue("id")));

		if (xml.getChild("learnset") != null) for (Element level : xml.getChild("learnset").getChildren())
			this.learnset.put(Integer.parseInt(level.getAttributeValue("l")), XMLUtils.readIntArrayAsList(level));
	}

	public PokemonSpecies(int id, int formID, PokemonType type1, PokemonType type2, int baseXP, ArrayList<PokemonStats> baseStats, float height, float weight,
			ArrayList<Integer> abilities, int[] experiencePerLevel, HashMap<Integer, ArrayList<Integer>> learnset, ArrayList<Integer> tms,
			ArrayList<Evolution> evolutions)
	{
		this.id = id;
		this.formID = formID;
		this.type1 = type1;
		this.type2 = type2;
		this.baseXP = baseXP;
		this.baseStats = baseStats;
		this.height = height;
		this.weight = weight;
		this.abilities = abilities;
		this.experiencePerLevel = experiencePerLevel;
		this.learnset = learnset;
		this.tms = tms;
		this.evolutions = evolutions;
	}

	public PokemonStats baseStatsIncrease(int level)
	{
		return this.baseStats.get(level);
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

	/** Generates a Pokémon of this species.
	 * 
	 * @param level - The level of the Pokémon to generate. */
	public Pokemon generate(Random random, int level)
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

		// todo: Generate random ID.
		return new Pokemon(0, this, null, null, this.statsForLevel(level), this.randomAbility(random), 0, level, move1, move2, move3, move4,
				this.randomGender(random));
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

	/** @return This Pokémon's name. */
	public Message name()
	{
		return new Message("pokemon." + this.id);
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

	/** @return Regular stats for a Pokémon at the input level. */
	public PokemonStats statsForLevel(int level)
	{
		PokemonStats stats = this.baseStats.get(0);
		for (int lvl = 1; lvl < level; ++lvl)
			stats.add(this.baseStats.get(lvl));
		return stats;
	}

	public Element toXML()
	{
		Element root = new Element("pokemon");
		root.setAttribute("id", Integer.toString(this.id));
		XMLUtils.setAttribute(root, "form-id", this.formID, 0);
		root.setAttribute("type1", Integer.toString(this.type1.id));
		if (this.type2 != null) root.setAttribute("type2", Integer.toString(this.type2.id));
		root.setAttribute("base-xp", Integer.toString(this.baseXP));
		root.setAttribute("height", Float.toString(this.height));
		root.setAttribute("weight", Float.toString(this.weight));

		int[][] line = new int[100][];
		for (int lvl = 0; lvl < this.baseStats.size(); lvl++)
		{
			PokemonStats stats = this.baseStats.get(lvl);
			if (stats.moveSpeed != 1) line[lvl] = new int[6];
			else line[lvl] = new int[5];

			line[lvl][PokemonStats.ATTACK] = stats.getAttack();
			line[lvl][PokemonStats.DEFENSE] = stats.getDefense();
			line[lvl][PokemonStats.HEALTH] = stats.getHealth();
			line[lvl][PokemonStats.SPECIAL_ATTACK] = stats.getSpecialAttack();
			line[lvl][PokemonStats.SPECIAL_DEFENSE] = stats.getSpecialDefense();
			if (stats.moveSpeed != 1) line[lvl][PokemonStats.SPEED] = stats.getMoveSpeed();
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

		return root;
	}

}

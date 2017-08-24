package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

public class PokemonSpecies
{

	/** Base experience. */
	public final int baseXP;
	/** List of species this Pokémon can evolve into. */
	private final ArrayList<Evolution> evolutions;
	public final float height, weight;
	public final int id, formID;
	/** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
	private final HashMap<Integer, ArrayList<Integer>> learnset;
	public final String name, formName;
	/** Recruitment rate. */
	public final int recruitRate;
	public final PokemonStats stats;
	/** List of TMs that can be taught. */
	private final ArrayList<Integer> tms;
	/** This Pokémon's types. type2 can be null. */
	public final PokemonType type1, type2;

	public PokemonSpecies(int id, String name, int formID, String formName, PokemonType type1, PokemonType type2, int baseXP, PokemonStats stats, float height,
			float weight, int recruitRate, HashMap<Integer, ArrayList<Integer>> learnset, ArrayList<Integer> tms, ArrayList<Evolution> evolutions)
	{
		this.id = id;
		this.name = name;
		this.formID = formID;
		this.formName = formName;
		this.type1 = type1;
		this.type2 = type2;
		this.baseXP = baseXP;
		this.stats = stats;
		this.height = height;
		this.weight = weight;
		this.recruitRate = recruitRate;
		this.learnset = learnset;
		this.tms = tms;
		this.evolutions = evolutions;
	}

	public Element toXML()
	{
		Element root = new Element("pokemon");
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("name", this.name);
		if (this.formID != 0) root.setAttribute("form-id", Integer.toString(this.formID));
		if (this.formName != null) root.setAttribute("form-name", this.formName);
		root.setAttribute("type1", Integer.toString(this.type1.id));
		if (this.type2 != null) root.setAttribute("type2", Integer.toString(this.type2.id));
		root.setAttribute("base-xp", Integer.toString(this.baseXP));
		root.setAttribute("height", Float.toString(this.height));
		root.setAttribute("weight", Float.toString(this.weight));
		root.setAttribute("recruit-rate", Integer.toString(this.recruitRate));

		root.addContent(this.stats.toXML());

		if (this.evolutions.size() != 0)
		{
			Element evolutions = new Element("evolves");
			for (Evolution evolution : this.evolutions)
				evolutions.addContent(evolution.toXML());
			root.addContent(evolutions);
		}

		if (this.tms.size() != 0)
		{
			Element tms = new Element("tms");
			for (Integer tm : this.tms)
				tms.addContent(new Element("tm").setAttribute("id", Integer.toString(tm)));
			root.addContent(tms);
		}

		if (this.learnset.size() != 0)
		{
			Element learnset = new Element("learnset");
			for (Integer level : this.learnset.keySet())
			{
				Element lv = new Element("level").setAttribute("l", Integer.toString(level));
				for (Integer move : this.learnset.get(level))
					lv.addContent(new Element("move").setAttribute("id", Integer.toString(move)));
				learnset.addContent(lv);
			}
			root.addContent(learnset);
		}

		return root;
	}

}

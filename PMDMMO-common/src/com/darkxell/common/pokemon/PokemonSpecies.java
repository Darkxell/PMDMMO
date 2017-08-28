package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.Message;
import com.darkxell.common.util.XMLUtils;

public class PokemonSpecies
{

	/** List of possible Abilities for this Pokémon. */
	private ArrayList<Integer> abilities;
	/** Base experience. */
	public final int baseXP;
	/** List of species this Pokémon can evolve into. */
	private final ArrayList<Evolution> evolutions;
	public final float height, weight;
	public final int id, formID;
	/** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
	private final HashMap<Integer, ArrayList<Integer>> learnset;
	public final PokemonStats stats;
	/** List of TMs that can be taught. */
	private final ArrayList<Integer> tms;
	/** This Pokémon's types. type2 can be null. */
	public final PokemonType type1, type2;

	/** Constructor for XML */
	public PokemonSpecies(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.formID = xml.getAttribute("form-id") == null ? 0 : Integer.parseInt(xml.getAttributeValue("id"));
		this.type1 = PokemonType.find(Integer.parseInt(xml.getAttributeValue("type1")));
		this.type2 = xml.getAttribute("type2") == null ? null : PokemonType.find(Integer.parseInt(xml.getAttributeValue("type2")));
		this.stats = new PokemonStats(xml.getChild("stats"));
		this.baseXP = Integer.parseInt(xml.getAttributeValue("base-xp"));
		this.height = Float.parseFloat(xml.getAttributeValue("height"));
		this.weight = Float.parseFloat(xml.getAttributeValue("weight"));
		this.abilities = XMLUtils.readIntArray(xml.getChild("abilities"));
		this.learnset = new HashMap<Integer, ArrayList<Integer>>();
		this.tms = XMLUtils.readIntArray(xml.getChild("tms"));
		this.evolutions = new ArrayList<Evolution>();

		if (xml.getChild("evolves") != null) for (Element e : xml.getChild("evolves").getChildren())
			this.evolutions.add(new Evolution(e));

		if (xml.getChild("tms") != null) for (Element tm : xml.getChild("tms").getChildren())
			this.tms.add(Integer.parseInt(tm.getAttributeValue("id")));

		if (xml.getChild("learnset") != null) for (Element level : xml.getChild("learnset").getChildren())
			this.learnset.put(Integer.parseInt(level.getAttributeValue("l")), XMLUtils.readIntArray(level));
	}

	public PokemonSpecies(int id, int formID, PokemonType type1, PokemonType type2, int baseXP, PokemonStats stats, float height, float weight,
			ArrayList<Integer> abilities, HashMap<Integer, ArrayList<Integer>> learnset, ArrayList<Integer> tms, ArrayList<Evolution> evolutions)
	{
		this.id = id;
		this.formID = formID;
		this.type1 = type1;
		this.type2 = type2;
		this.baseXP = baseXP;
		this.stats = stats;
		this.height = height;
		this.weight = weight;
		this.abilities = abilities;
		this.learnset = learnset;
		this.tms = tms;
		this.evolutions = evolutions;
	}

	/** @return This Pokémon's name. */
	public Message name()
	{
		return new Message("pokemon." + this.id);
	}

	public Element toXML()
	{
		Element root = new Element("pokemon");
		root.setAttribute("id", Integer.toString(this.id));
		if (this.formID != 0) root.setAttribute("form-id", Integer.toString(this.formID));
		root.setAttribute("type1", Integer.toString(this.type1.id));
		if (this.type2 != null) root.setAttribute("type2", Integer.toString(this.type2.id));
		root.setAttribute("base-xp", Integer.toString(this.baseXP));
		root.setAttribute("height", Float.toString(this.height));
		root.setAttribute("weight", Float.toString(this.weight));

		root.addContent(this.stats.toXML());

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

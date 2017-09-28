package com.darkxell.common.dungeon;

import org.jdom2.Element;

import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;

/** Describes how a Pokémon appears in a Dungeon. */
public class DungeonEncounter
{
	public static final String XML_ROOT = "pokemon";

	/** The floors this Pokémon can appear on. */
	public final FloorSet floors;
	/** The Pokémon ID. */
	public final int id;
	/** The Level of the Pokémon. */
	public final int level;

	public DungeonEncounter(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.level = Integer.parseInt(xml.getAttributeValue("level"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
	}

	public DungeonEncounter(int id, int level, FloorSet floors)
	{
		this.id = id;
		this.level = level;
		this.floors = floors;
	}

	public PokemonSpecies pokemon()
	{
		return PokemonRegistry.find(this.id);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("level", Integer.toString(this.level));
		root.addContent(this.floors.toXML());
		return root;
	}

}

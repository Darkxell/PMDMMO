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
	/** True if this Pokémon can be recruited. If so, {@link DungeonEncounter#recruitRate} is its recruit rate. */
	public final boolean isRecruitable;
	/** The Level of the Pokémon. */
	public final int level;
	/** The recruit rate of the Pokémon. */
	public final float recruitRate;

	public DungeonEncounter(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.level = Integer.parseInt(xml.getAttributeValue("level"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));

		String recruit = xml.getAttributeValue("recruit");
		this.isRecruitable = recruit != null;
		this.recruitRate = recruit == null ? 0 : Float.parseFloat(recruit);
	}

	public DungeonEncounter(int id, int level, FloorSet floors, boolean isRecruitable, float recruitRate)
	{
		this.id = id;
		this.level = level;
		this.floors = floors;
		this.isRecruitable = isRecruitable;
		this.recruitRate = recruitRate;
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
		if (this.isRecruitable) root.setAttribute("recruit", Float.toString(this.recruitRate));
		root.addContent(this.floors.toXML());
		return root;
	}

}

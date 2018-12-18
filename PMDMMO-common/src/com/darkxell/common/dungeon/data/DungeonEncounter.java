package com.darkxell.common.dungeon.data;

import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

/** Describes how a Pokemon appears in a Dungeon. */
public class DungeonEncounter
{
	public static final String XML_ROOT = "pokemon";

	/** The floors this Pokemon can appear on. */
	public final FloorSet floors;
	/** The Pokemon ID. */
	public final int id;
	/** The Level of the Pokemon. */
	public final int level;
	/** The weight of the encounter. */
	public final int weight;

	public DungeonEncounter(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.level = Integer.parseInt(xml.getAttributeValue("level"));
		this.weight = XMLUtils.getAttribute(xml, "weight", 1);
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace()));
	}

	public DungeonEncounter(int id, int level, int weight, FloorSet floors)
	{
		this.id = id;
		this.level = level;
		this.floors = floors;
		this.weight = weight;
	}

	public PokemonSpecies pokemon()
	{
		return Registries.species().find(this.id);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("level", Integer.toString(this.level));
		if (this.weight != 1) root.setAttribute("weight", Integer.toString(this.weight));
		root.addContent(this.floors.toXML());
		return root;
	}

}

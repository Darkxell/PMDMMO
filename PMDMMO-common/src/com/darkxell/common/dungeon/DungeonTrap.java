package com.darkxell.common.dungeon;

import org.jdom2.Element;

import com.darkxell.common.trap.Trap;
import com.darkxell.common.trap.TrapRegistry;

/** Describes how an Trap appears in a Dungeon. */
public class DungeonTrap
{
	public static final String XML_ROOT = "trap";

	/** The floors this Trap can appear on. */
	public final FloorSet floors;
	/** The Trap ID. */
	public final int id;
	/** The weight of the Trap. Represents how likely it is to appear compared to other traps on this floor. */
	public final int weight;

	public DungeonTrap(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.weight = Integer.parseInt(xml.getAttributeValue("weight"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
	}

	public DungeonTrap(int id, int weight, FloorSet floors)
	{
		this.id = id;
		this.weight = weight;
		this.floors = floors;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("weight", Integer.toString(this.weight));
		root.addContent(this.floors.toXML());
		return root;
	}

	public Trap trap()
	{
		return TrapRegistry.find(this.id);
	}

}
